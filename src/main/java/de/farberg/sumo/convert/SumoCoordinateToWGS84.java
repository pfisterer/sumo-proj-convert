package de.farberg.sumo.convert;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniluebeck.itm.util.logging.LogLevel;
import de.uniluebeck.itm.util.logging.Logging;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.coords.UTMCoord;

public class SumoCoordinateToWGS84 {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws XMLStreamException, IOException {
		CommandLineOptions options = CommandLineOptions.parseCmdLineOptions(args, SumoCoordinateToWGS84.class);
		Logging.setLoggingDefaults(options.verbose ? LogLevel.DEBUG : LogLevel.INFO, "[%-5p; %c{1}::%M] %m%n");
		Logger log = LoggerFactory.getLogger(SumoCoordinateToWGS84.class);

		XMLInputFactory inFactory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = inFactory.createXMLEventReader(new FileInputStream(options.inFile));

		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLEventWriter writer = factory.createXMLEventWriter(new FileWriter(options.outFile));

		XMLEventFactory eventFactory = XMLEventFactory.newInstance();

		String hemisphere = ("NORTH".equals(options.hemisphere)) ? AVKey.NORTH : AVKey.SOUTH;

		while (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();

			// <vehicle id="0" eclass="HBEFA2/P_7_7" CO2="1449.01" CO="18.23" HC="0.59" NOx="2.71" PMx="0.13" fuel="0.58" electricity="0.00"
			// noise="64.71" route="!0" type="DEFAULT_VEHTYPE" waiting="0.00" lane="29276269#0_0" pos="6.93" speed="1.83" angle="140.06"
			// x="5402.94" y="12987.23"/>
			if (event.getEventType() == XMLEvent.START_ELEMENT
					&& event.asStartElement().getName().getLocalPart().equals("vehicle")) {
				double x = Double.parseDouble(event.asStartElement().getAttributeByName(new QName("x")).getValue());
				double y = Double.parseDouble(event.asStartElement().getAttributeByName(new QName("y")).getValue());

				LatLon locationFromUTMCoord = UTMCoord.locationFromUTMCoord(options.zone, hemisphere, x - options.offsetx,
						y - options.offsety);

				log.debug("Converting x={}, y={} --> {}", x, y, locationFromUTMCoord);

				Attribute lat = eventFactory.createAttribute("latitude", "" + locationFromUTMCoord.asDegreesArray()[0]);
				Attribute lon = eventFactory.createAttribute("longitude", "" + locationFromUTMCoord.asDegreesArray()[1]);

				List listOfAttributes = new ArrayList<>();
				Iterator iterator = event.asStartElement().getAttributes();
				while (iterator.hasNext()) {
					Object next = iterator.next();
					listOfAttributes.add(next);
				}

				listOfAttributes.add(lat);
				listOfAttributes.add(lon);

				StartElement newStartElement = eventFactory.createStartElement(event.asStartElement().getName(),
						listOfAttributes.iterator(), null);

				writer.add(newStartElement);

				log.debug("Added new start element: {}", newStartElement);
			} else {
				writer.add(event);
			}
		}

		writer.close();
		log.info("Done.");

	}
}
