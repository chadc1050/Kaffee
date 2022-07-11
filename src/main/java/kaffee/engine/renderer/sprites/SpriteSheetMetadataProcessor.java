package kaffee.engine.renderer.sprites;

import kaffee.engine.renderer.Texture;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;

import javax.management.modelmbean.XMLParseException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Float.parseFloat;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

@Getter
@Setter
public class SpriteSheetMetadataProcessor
{
    private File metadata;
    private List<Sprite> sprites = new ArrayList<>();
    private String name;
    private Texture texture;

    public SpriteSheetMetadataProcessor(String filePath, Texture texture)
    {
        this.texture = texture;
        this.metadata = new File(filePath);
    }

    public void parse()
    {
        try(FileInputStream fileInputStream = new FileInputStream(metadata))
        {
            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(fileInputStream);
            while (reader.hasNext())
            {
                switch (reader.next())
                {
                    case START_ELEMENT:
                        if("name".equals(reader.getLocalName()))
                        {
                            setName(readText(reader));
                        }
                        if("sprite".equals(reader.getLocalName()))
                        {
                            sprites.add(readSprite(reader));
                        }
                        break;
                    case END_ELEMENT:
                        break;
                }
            }
        }
        catch (IOException | XMLStreamException | XMLParseException e)
        {
            System.err.println("ERROR: Could not parse Sprite Sheet Metadata for '" + metadata.getName() + "'");
            e.printStackTrace();
        }
    }

    private Sprite readSprite(XMLStreamReader reader) throws XMLStreamException, XMLParseException
    {
        Sprite sprite = new Sprite();
        sprite.setTexture(texture);
        float rightX = -1.0f, leftX = -1.0f, topY = -1.0f, bottomY = -1.0f;

        while(reader.hasNext())
        {
            switch (reader.next())
            {
                case START_ELEMENT:
                    if ("topY".equals(reader.getLocalName()))
                    {
                        topY = readFloat(reader);
                    }
                    if ("bottomY".equals(reader.getLocalName()))
                    {
                        bottomY = readFloat(reader);
                    }
                    if ("rightX".equals(reader.getLocalName()))
                    {
                        rightX = readFloat(reader);
                    }
                    if ("leftX".equals(reader.getLocalName()))
                    {
                        leftX = readFloat(reader);
                    }
                    break;
                case END_ELEMENT:
                    if (!areCoordinatesPresent(topY, bottomY, leftX, rightX))
                    {
                        throw new IllegalStateException("ERROR: Sprite coordinates are non-nullable.");
                    }
                    sprite.setWidth(rightX - leftX);
                    sprite.setHeight(topY - bottomY);

                    topY /= texture.getHeight();
                    bottomY /= texture.getHeight();
                    leftX /= texture.getWidth();
                    rightX /= texture.getWidth();

                    sprite.setTextureCoordinates(new Vector2f[] {
                            new Vector2f(rightX, topY),
                            new Vector2f(rightX, bottomY),
                            new Vector2f(leftX, bottomY),
                            new Vector2f(leftX, topY)
                    });
                    return sprite;
            }
        }
        throw new XMLStreamException("Unexpected end of element.");
    }

    private String readText(XMLStreamReader reader) throws XMLStreamException, XMLParseException
    {
        StringBuilder result = new StringBuilder();
        while (reader.hasNext())
        {
            switch (reader.next())
            {
                case XMLStreamReader.CHARACTERS:
                    result.append(reader.getText());
                    break;
                case XMLStreamReader.END_ELEMENT:
                    return result.toString();
            }
        }
        throw new XMLStreamException("Unexpected end of element.");
    }

    private float readFloat(XMLStreamReader reader) throws XMLStreamException, XMLParseException
    {
        return parseFloat(readText(reader));
    }

    private boolean areCoordinatesPresent(float topY, float bottomY, float leftX, float rightX)
    {
        return topY != -1 && bottomY != -1 && leftX != -1 && rightX != -1;
    }
}
