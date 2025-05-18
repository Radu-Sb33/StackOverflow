package serializer;

import com.codeelevate.stackoverflow_spring.entity.Post;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class PostIdSerializer extends JsonSerializer<Post> {
    @Override
    public void serialize(Post post, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (post != null) {
            gen.writeNumber(post.getId()); // Scrie doar ID-ul postării
        } else {
            gen.writeNull();
        }
    }
}