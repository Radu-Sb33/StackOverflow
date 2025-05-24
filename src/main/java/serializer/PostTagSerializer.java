package serializer;

import com.codeelevate.stackoverflow_spring.entity.PostTag;
import com.codeelevate.stackoverflow_spring.entity.Tag;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;


public class PostTagSerializer extends JsonSerializer<PostTag> {
    @Override
    public void serialize(PostTag postTag, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", postTag.getId());

        // Trebuie să serializezi și tag-ul complet aici
        Tag tag = postTag.getTag();
        if (tag != null) {
            gen.writeObjectFieldStart("tag");
            gen.writeNumberField("id", tag.getId());
            gen.writeStringField("tagName", tag.getTagName());
            // Include orice alte câmpuri vrei
            gen.writeEndObject();
        }

        gen.writeEndObject();
    }
}
