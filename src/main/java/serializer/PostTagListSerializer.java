package serializer;

import com.codeelevate.stackoverflow_spring.entity.PostTag;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.util.List;
import com.codeelevate.stackoverflow_spring.entity.PostTag;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class PostTagListSerializer extends JsonSerializer<List<PostTag>> {
    @Override
    public void serialize(List<PostTag> postTags, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        for (PostTag pt : postTags) {
            gen.writeNumber(pt.getId());
        }
        gen.writeEndArray();
    }
}


