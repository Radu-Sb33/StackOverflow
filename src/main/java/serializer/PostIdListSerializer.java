package serializer;

import com.codeelevate.stackoverflow_spring.entity.Post;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

public class PostIdListSerializer extends JsonSerializer<List<Post>> {
    @Override
    public void serialize(List<Post> posts, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        if (posts != null) {
            for (Post post : posts) {
                if (post != null) {
                    gen.writeNumber(post.getId()); // Scrie doar ID-ul postării
                } else {
                    gen.writeNull();
                }
            }
        }
        gen.writeEndArray();
    }
}