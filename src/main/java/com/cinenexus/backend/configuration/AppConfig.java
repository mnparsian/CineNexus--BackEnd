package com.cinenexus.backend.configuration;

import com.cinenexus.backend.ai.MovieRecommendationService;
import com.cinenexus.backend.ai.RecommendationController;
import com.cinenexus.backend.ai.RecommendationService;

import com.cinenexus.backend.dto.chat.MessageMapper;
import com.cinenexus.backend.dto.comment.CommentMapper;
import com.cinenexus.backend.dto.commentlike.CommentLikeMapper;
import com.cinenexus.backend.dto.favoriteMovie.FavoriteMovieResponseDTO;
import com.cinenexus.backend.dto.friendship.FriendRequestResponseDTO;
import com.cinenexus.backend.dto.friendship.FriendResponseDTO;
import com.cinenexus.backend.dto.media.MediaResponseDTO;
import com.cinenexus.backend.dto.payment.PaymentMapper;
import com.cinenexus.backend.dto.review.ReviewLikeResponseDTO;
import com.cinenexus.backend.dto.review.ReviewMapper;
import com.cinenexus.backend.dto.subscription.SubscriptionMapper;
import com.cinenexus.backend.dto.watchList.WatchListResponseDTO;
import com.cinenexus.backend.model.payment.Payment;
import com.cinenexus.backend.repository.CommentRepository;
import com.cinenexus.backend.repository.MediaRepository;
import com.cinenexus.backend.repository.ReviewRepository;
import com.cinenexus.backend.repository.UserRepository;
import com.cinenexus.backend.service.OpenAIService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public PaymentMapper paymentMapper() {
    return new PaymentMapper();
  }

  @Bean
  public SubscriptionMapper subscriptionMapper() {
    return new SubscriptionMapper();
  }

  @Bean
  public ReviewMapper reviewMapper(UserRepository userRepository, MediaRepository mediaRepository) {
    return new ReviewMapper(userRepository, mediaRepository);
  }

  @Bean
  public CommentMapper commentMapper(
      UserRepository userRepository,
      ReviewRepository reviewRepository,
      MediaRepository mediaRepository,
      CommentRepository commentRepository) {
    return new CommentMapper(userRepository, reviewRepository, mediaRepository, commentRepository);
  }

  @Bean
  public CommentLikeMapper commentLikeMapper() {
    return new CommentLikeMapper();
  }

  @Bean
  public MovieRecommendationService movieRecommendationService(
      MediaRepository mediaRepository,
      UserRepository userRepository,
      MediaResponseDTO mediaResponseDTO) {
    return new MovieRecommendationService(mediaRepository, userRepository, mediaResponseDTO);
  }

  //    @Bean public TVShowRecommendationService tvShowRecommendationService(MediaRepository
  // mediaRepository,UserRepository userRepository){
  //        return new TVShowRecommendationService(mediaRepository,userRepository);
  //    }
  @Bean
  public RecommendationService recommendationService(
      MediaRepository mediaRepository, MediaResponseDTO mediaResponseDTO) {
    return new RecommendationService(mediaRepository, mediaResponseDTO);
  }

  @Bean
  public RecommendationController recommendationController(
          RecommendationService recommendationService,
          MovieRecommendationService movieRecommendationService, OpenAIService openAIService) {
    return new RecommendationController(recommendationService, movieRecommendationService, openAIService);
  }

  @Bean
  public ReviewLikeResponseDTO reviewLikeResponseDTO() {
    return new ReviewLikeResponseDTO();
  }

  @Bean
  public MediaResponseDTO mediaResponseDTO() {
    return new MediaResponseDTO();
  }

  @Bean
  public WatchListResponseDTO watchListResponseDTO(){
    return new WatchListResponseDTO();
  }
  @Bean
  public FavoriteMovieResponseDTO favoriteMovieResponseDTO(){
    return new FavoriteMovieResponseDTO();
  }
  @Bean
  public MessageMapper messageMapper(){
    return new MessageMapper();
  }
  @Bean
  public FriendResponseDTO friendResponseDTO(){
    return new FriendResponseDTO();
  }
  @Bean
  public FriendRequestResponseDTO friendRequestResponseDTO(){
    return new FriendRequestResponseDTO();
  }
}
