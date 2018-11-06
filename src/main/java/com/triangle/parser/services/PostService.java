package com.triangle.parser.services;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.triangle.parser.dao.PostRepository;
import com.triangle.parser.models.Post;
import com.triangle.parser.models.Task;

@Service
public class PostService {
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private InstaCrawler instaCrawler;
	
	/**
	 * Searches posts in Instagram by given task, saves posts to the db.
	 * @param task
	 */
	public void applyTask(Task task) {
		List<List<Post>> list = new ArrayList<>();
		task.getHashtags().forEach(hashtag -> {
			list.add(instaCrawler.getPosts(hashtag, task.getPostCount()));
		});
		
		
		List<Post> posts = new ArrayList<Post>();
		list.forEach(posts::addAll);
		
		posts.forEach(post -> post.setTaskId(task.getId()));
		postRepository.saveAll(posts);
	}
	
	public List<Post> findByTaskId(ObjectId id){
		return postRepository.findByTaskId(id);
	}

}
