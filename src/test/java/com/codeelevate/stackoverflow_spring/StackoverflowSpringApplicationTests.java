package com.codeelevate.stackoverflow_spring;

import com.codeelevate.stackoverflow_spring.entity.*;
import com.codeelevate.stackoverflow_spring.repository.*;
import com.codeelevate.stackoverflow_spring.service.*;
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

	@Test
	void contextLoads() {
		// Ensure the application context loads successfully
		assertNotNull(userService);
		assertNotNull(userRepository);
		assertNotNull(postService);
		assertNotNull(postRepository);
	}

	@BeforeEach
	void setUp() {
		// Create a test user
		User user = new User();
		user.setUsername("testuser");
		user.setEmail("test@example.com");
		user.setPassword("password");
		testUser = userService.createUser(user);
	}


	@Test
	void testCreateUser() {
		// Arrange
		User user = new User();
		user.setUsername("testuser");
		user.setEmail("test@example.com");
		user.setPassword("password");

		// Act
		User createdUser = userService.createUser(user);

		// Assert
		assertNotNull(createdUser.getId());
		assertEquals("testuser", createdUser.getUsername());
		assertEquals("test@example.com", createdUser.getEmail());

		// Verify the user exists in the repository
		Optional<User> fetchedUser = userRepository.findById(createdUser.getId());
		assertTrue(fetchedUser.isPresent());
		assertEquals("testuser", fetchedUser.get().getUsername());
	}

//	@Test
//	void testCreatePost() {
//		// Arrange
//		User user = new User();
//		user.setUsername("postuser");
//		user.setEmail("postuser@example.com");
//		user.setPassword("password");
//		User savedUser = userService.createUser(user);
//
//		Post post = new Post();
//		post.setPostType(postTypeRepository.findByPostTypeName("question"));
//		post.setPostTitleQ("Test Post");
//		post.setPostContent("This is a test post.");
//		post.setCreatedByUser(savedUser);
//
//		// Act
//		Post createdPost = postService.createPost(post);
//
//		// Assert
//		assertNotNull(createdPost.getId());
//		assertEquals("Test Post", createdPost.getPostTitleQ());
//		assertEquals("This is a test post.", createdPost.getPostContent());
//		assertNotNull(createdPost.getCreatedByUser());
//		assertEquals(savedUser.getId(), createdPost.getCreatedByUser().getId());
//	}

//	@Test
//	void testCreateQuestionAndAnswer() {
//		// Arrange: Create a question
//		Post question = new Post();
//		PostType questionType = postTypeRepository.findByPostTypeName("question");
//		question.setPostType(questionType);
//		question.setPostTitleQ("What is Java?");
//		question.setPostContent("Can someone explain what Java is?");
//		question.setCreatedByUser(testUser);
//
//		// Save the question
//		Post createdQuestion = postService.createPost(question);
//
//		// Assert the question was created successfully
//		assertNotNull(createdQuestion.getId());
//		assertEquals("What is Java?", createdQuestion.getPostTitleQ());
//		assertEquals("Can someone explain what Java is?", createdQuestion.getPostContent());
//		assertNotNull(createdQuestion.getCreatedByUser());
//		assertEquals(testUser.getId(), createdQuestion.getCreatedByUser().getId());
//		// Arrange: Create an answer for the question
//		Post answer = new Post();
//		PostType answerType = postTypeRepository.findByPostTypeName("answer");
//		answer.setPostType(answerType);
//		answer.setPostContent("Java is a programming language.");
//		answer.setCreatedByUser(testUser);
//		answer.setParentQuestion(createdQuestion); // Link the answer to the question
//
//		// Save the answer
//		Post createdAnswer = postService.createPost(answer);
//
//		// Assert the answer was created successfully
//		assertNotNull(createdAnswer.getId());
//		assertEquals("Java is a programming language.", createdAnswer.getPostContent());
//		assertNotNull(createdAnswer.getCreatedByUser());
//		assertEquals(testUser.getId(), createdAnswer.getCreatedByUser().getId());
//		assertNotNull(createdAnswer.getParentQuestion());
//		assertEquals(createdQuestion.getId(), createdAnswer.getParentQuestion().getId());
//
//		// Verify the relationship between the question and the answer
//		Optional<Post> fetchedQuestion = postRepository.findById(createdQuestion.getId());
//		assertTrue(fetchedQuestion.isPresent());
//		assertEquals(1, fetchedQuestion.get().getAnswers().size());
//		assertEquals(createdAnswer.getId(), fetchedQuestion.get().getAnswers().get(0).getId());
//	}
}