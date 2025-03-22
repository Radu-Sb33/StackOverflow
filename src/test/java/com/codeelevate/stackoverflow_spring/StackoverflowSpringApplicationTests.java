package com.codeelevate.stackoverflow_spring;

import com.codeelevate.stackoverflow_spring.entity.*;
import com.codeelevate.stackoverflow_spring.repository.*;
import com.codeelevate.stackoverflow_spring.service.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StackoverflowSpringApplicationTests {

	@Autowired
	private UserService userService;

	@Autowired
	private PostService postService;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IPostRepository postRepository;

	@Autowired
	private IPostTypeRepository postTypeRepository;

	private User testUser;

	private Post testQuestion;

	@Test
	@BeforeEach
	@Transactional
	void setUp() {
		User user = new User();
		user.setUsername("testuser");
		user.setEmail("test@example.com");
		user.setPassword("password");
		testUser = userService.createUser(user);
		assertNotNull(testUser.getId());
		assertEquals("testuser", testUser.getUsername());
		assertEquals("test@example.com", testUser.getEmail());
		assertTrue(PasswordEncryptionUtil.verifyPassword("password",testUser.getPassword()));

		Post question = new Post();
		PostType questionType = postTypeRepository.findByPostTypeName("question");
		question.setPostType(questionType);
		question.setPostTitleQ("What is Java?");
		question.setPostContent("Can someone explain what Java is?");
		question.setCreatedByUser(testUser);

		testQuestion = postService.createPost(question);

		assertNotNull(testQuestion.getId());
		assertEquals("What is Java?", testQuestion.getPostTitleQ());
		assertEquals("Can someone explain what Java is?", testQuestion.getPostContent());
		assertNotNull(testQuestion.getCreatedByUser());
		assertEquals(testUser.getId(), testQuestion.getCreatedByUser().getId());
	}


	@Test
	@Transactional
	void testCreateUser() {
		User user = new User();
		user.setUsername("testuser");
		user.setEmail("test@example.com");
		user.setPassword("password");
		User createdUser = userService.createUser(user);

		assertNotNull(createdUser.getId());
		assertEquals("testuser", createdUser.getUsername());
		assertEquals("test@example.com", createdUser.getEmail());
		assertTrue(PasswordEncryptionUtil.verifyPassword("password",createdUser.getPassword()));

		Optional<User> fetchedUser = userRepository.findById(createdUser.getId());
		assertTrue(fetchedUser.isPresent());
		assertEquals("testuser", fetchedUser.get().getUsername());
	}

	@Test
	@Transactional
	void testUpdateUser() {
		User user = new User();
		user.setUsername("testuser");
		user.setEmail("test@example.com");
		user.setPassword("password");
		User savedUser = userService.createUser(user);

		assertNotNull(savedUser.getId());
		assertEquals("testuser", savedUser.getUsername());
		assertEquals("test@example.com", savedUser.getEmail());
		assertTrue(PasswordEncryptionUtil.verifyPassword("password",savedUser.getPassword()));

		User updatedUser = new User();
		updatedUser.setUsername("updatedusername");
		updatedUser.setEmail("updatedemail@example.com");
		updatedUser.setPassword("newpassword");
		User result = userService.updateUser(savedUser.getId(), updatedUser);

		assertNotNull(result);
		assertEquals("updatedusername", result.getUsername());
		assertEquals("updatedemail@example.com", result.getEmail());
		assertTrue(PasswordEncryptionUtil.verifyPassword("newpassword",result.getPassword()));

		Optional<User> fetchedUser = userRepository.findById(savedUser.getId());
		assertTrue(fetchedUser.isPresent());
		assertEquals("updatedusername", fetchedUser.get().getUsername());
		assertEquals("updatedemail@example.com", fetchedUser.get().getEmail());
		assertTrue(PasswordEncryptionUtil.verifyPassword("newpassword",fetchedUser.get().getPassword()));
	}

	@Test
	@Transactional
	void testDeleteUser() {
		User user = new User();
		user.setUsername("testuser");
		user.setEmail("test@example.com");
		user.setPassword("password");
		User savedUser = userService.createUser(user);

		assertNotNull(savedUser.getId());
		assertEquals("testuser", savedUser.getUsername());
		assertEquals("test@example.com", savedUser.getEmail());
		assertTrue(PasswordEncryptionUtil.verifyPassword("password",savedUser.getPassword()));

		userService.deleteUser(savedUser.getId());

		Optional<User> fetchedUser = userRepository.findById(savedUser.getId());
		assertFalse(fetchedUser.isPresent());
	}

	@Test
	@Transactional
	void testCreateQuestion() {
		Post post = new Post();
		post.setPostType(postTypeRepository.findByPostTypeName("question"));
		post.setPostTitleQ("Test Post");
		post.setPostContent("This is a test post.");
		post.setCreatedByUser(testUser);
		testUser.getPosts().add(post);

		Post createdPost = postService.createPost(post);

		assertNotNull(createdPost.getId());
		assertEquals("Test Post", createdPost.getPostTitleQ());
		assertEquals("This is a test post.", createdPost.getPostContent());
		assertEquals("question", createdPost.getPostType().getTypeName());
		assertNotNull(createdPost.getCreatedByUser());
		assertEquals(testUser.getId(), createdPost.getCreatedByUser().getId());



		Optional<Post> fetchedPost = postRepository.findById(createdPost.getId());
		assertTrue(fetchedPost.isPresent());
		assertEquals("Test Post", fetchedPost.get().getPostTitleQ());
		assertEquals("This is a test post.", fetchedPost.get().getPostContent());
		assertEquals("question", fetchedPost.get().getPostType().getTypeName());
		assertEquals(testUser.getId(), fetchedPost.get().getCreatedByUser().getId());
	}

	@Test
	@Transactional
	void testUpdateQuestion() {
		Post post = new Post();
		post.setPostType(postTypeRepository.findByPostTypeName("question"));
		post.setPostTitleQ("Test Post");
		post.setPostContent("This is a test post.");
		post.setCreatedByUser(testUser);
		Post createdPost = postService.createPost(post);

		assertNotNull(createdPost.getId());
		assertEquals("Test Post", createdPost.getPostTitleQ());
		assertEquals("This is a test post.", createdPost.getPostContent());
		assertEquals("question", createdPost.getPostType().getTypeName());
		assertNotNull(createdPost.getCreatedByUser());
		assertEquals(testUser.getId(), createdPost.getCreatedByUser().getId());


		Post updatedPost = new Post();
		updatedPost.setPostTitleQ("New Title Post");
		updatedPost.setPostContent("This is an updated post.");

		Post result = postService.updatePost(createdPost.getId(), updatedPost);

		assertNotNull(result);
		assertEquals("New Title Post", result.getPostTitleQ());
		assertEquals("This is an updated post.", result.getPostContent());

		Optional<Post> fetchedPost = postRepository.findById(createdPost.getId());
		assertTrue(fetchedPost.isPresent());
		assertEquals("New Title Post", fetchedPost.get().getPostTitleQ());
		assertEquals("This is an updated post.", fetchedPost.get().getPostContent());
		assertEquals("question", fetchedPost.get().getPostType().getTypeName());
		assertEquals(testUser.getId(), fetchedPost.get().getCreatedByUser().getId());
	}
	@Test
	@Transactional
	void testDeleteQuestion(){
		Post post = new Post();
		post.setPostType(postTypeRepository.findByPostTypeName("question"));
		post.setPostTitleQ("Test Post");
		post.setPostContent("This is a test post.");
		post.setCreatedByUser(testUser);
		Post createdPost = postService.createPost(post);

		Post answer1 = new Post();
		answer1.setPostType(postTypeRepository.findByPostTypeName("answer"));
		answer1.setPostContent("This is the first answer.");
		answer1.setCreatedByUser(testUser);
		answer1.setParentQuestion(createdPost);

		Post answer2 = new Post();
		answer2.setPostType(postTypeRepository.findByPostTypeName("answer"));
		answer2.setPostContent("This is the second answer.");
		answer2.setCreatedByUser(testUser);
		answer2.setParentQuestion(createdPost);

		Post createdAnswer1 = postService.createPost(answer1);
		Post createdAnswer2 = postService.createPost(answer2);

		assertNotNull(createdAnswer1.getId());
		assertNotNull(createdAnswer2.getId());

		assertNotNull(createdPost.getId());
		assertEquals("Test Post", createdPost.getPostTitleQ());
		assertEquals("This is a test post.", createdPost.getPostContent());
		assertEquals("question", createdPost.getPostType().getTypeName());
		assertNotNull(createdPost.getCreatedByUser());
		assertEquals(testUser.getId(), createdPost.getCreatedByUser().getId());

		postService.deletePost(createdPost.getId());
		
		Optional<Post> fetchedPost = postRepository.findById(createdPost.getId());
		Optional<Post> fetchedAnswer1 = postRepository.findById(createdAnswer1.getId());
		Optional<Post> fetchedAnswer2 = postRepository.findById(createdAnswer2.getId());

		assertFalse(fetchedAnswer1.isPresent());
		assertFalse(fetchedAnswer2.isPresent());
		assertFalse(fetchedPost.isPresent());
	}

	@Test
	@Transactional
	void testCreateAnswer() {
		Post answer = new Post();
		PostType answerType = postTypeRepository.findByPostTypeName("answer");
		answer.setPostType(answerType);
		answer.setPostContent("Java is a programming language.");
		answer.setCreatedByUser(testUser);
		answer.setParentQuestion(testQuestion);

		Post createdAnswer = postService.createPost(answer);

		assertNotNull(createdAnswer.getId());
		assertEquals("Java is a programming language.", createdAnswer.getPostContent());
		assertNotNull(createdAnswer.getCreatedByUser());
		assertEquals(testUser.getId(), createdAnswer.getCreatedByUser().getId());
		assertNotNull(createdAnswer.getParentQuestion());
		assertEquals(testQuestion.getId(), createdAnswer.getParentQuestion().getId());
		assertEquals("answer", createdAnswer.getPostType().getTypeName());

		Optional<Post> fetchedAnswer = postRepository.findById(createdAnswer.getId());
		assertTrue(fetchedAnswer.isPresent());
		assertEquals("Java is a programming language.", fetchedAnswer.get().getPostContent());
		assertEquals(testQuestion.getId(), fetchedAnswer.get().getParentQuestion().getId());
		assertEquals(fetchedAnswer.get().getId(), testQuestion.getAnswers().getFirst().getId());
	}

	@Test
	@Transactional
	void testUpdateAnswer() {
		Post answer = new Post();
		PostType answerType = postTypeRepository.findByPostTypeName("answer");
		answer.setPostType(answerType);
		answer.setPostContent("Java is a programming language.");
		answer.setCreatedByUser(testUser);
		answer.setParentQuestion(testQuestion);

		Post createdAnswer = postService.createPost(answer);

		assertNotNull(createdAnswer.getId());
		assertEquals("Java is a programming language.", createdAnswer.getPostContent());
		assertNotNull(createdAnswer.getCreatedByUser());
		assertEquals(testUser.getId(), createdAnswer.getCreatedByUser().getId());
		assertNotNull(createdAnswer.getParentQuestion());
		assertEquals(testQuestion.getId(), createdAnswer.getParentQuestion().getId());
		assertEquals("answer", createdAnswer.getPostType().getTypeName());

		Post updatedAnswer = new Post();
		updatedAnswer.setPostContent("This is an updated post.");
		Post result = postService.updatePost(createdAnswer.getId(), updatedAnswer);
		assertNotNull(result);
		assertEquals("This is an updated post.", result.getPostContent());

		Optional<Post> fetchedAnswer = postRepository.findById(createdAnswer.getId());
		assertTrue(fetchedAnswer.isPresent());
		assertEquals("This is an updated post.", fetchedAnswer.get().getPostContent());
		assertEquals(testQuestion.getId(), fetchedAnswer.get().getParentQuestion().getId());
		assertEquals(fetchedAnswer.get().getId(), testQuestion.getAnswers().getFirst().getId());
	}

	@Test
	@Transactional
	void testDeleteAnswer() {
		Post answer = new Post();
		PostType answerType = postTypeRepository.findByPostTypeName("answer");
		answer.setPostType(answerType);
		answer.setPostContent("Java is a programming language.");
		answer.setCreatedByUser(testUser);
		answer.setParentQuestion(testQuestion);
		Post createdAnswer = postService.createPost(answer);

		assertNotNull(createdAnswer.getId());
		assertEquals("Java is a programming language.", createdAnswer.getPostContent());
		assertNotNull(createdAnswer.getCreatedByUser());
		assertEquals(testUser.getId(), createdAnswer.getCreatedByUser().getId());
		assertNotNull(createdAnswer.getParentQuestion());
		assertEquals(testQuestion.getId(), createdAnswer.getParentQuestion().getId());
		assertEquals("answer", createdAnswer.getPostType().getTypeName());

		postService.deletePost(createdAnswer.getId());

		Optional<Post> fetchedAnswer = postRepository.findById(createdAnswer.getId());
		assertFalse(fetchedAnswer.isPresent());
	}
}