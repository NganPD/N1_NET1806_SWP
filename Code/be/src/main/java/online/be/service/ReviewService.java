package online.be.service;

import online.be.entity.Review;
import online.be.exception.BadRequestException;
import online.be.model.Request.VenueReviewRequest;
import online.be.repository.AccountRepostory;
import online.be.repository.AuthenticationRepository;
import online.be.repository.ReviewRepository;
import online.be.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    AccountRepostory accountRepostory;

    @Autowired
    VenueRepository venueRepository;

    public Review createReview(VenueReviewRequest request){

        Review review = new Review();
        review.setComment(request.getComment());
        review.setCreatedDate(LocalDateTime.now());
        review.setRating(review.getRating());
        review.setAccount(accountRepostory.findById(request.getUserId()).orElseThrow(null));
        review.setVenue(venueRepository.findById(request.getVenueId()).orElseThrow(null));

        return reviewRepository.save(review);
    }

    public Review getReviewById(long reviewId){
        return reviewRepository.findById(reviewId)
                .orElseThrow(()-> new BadRequestException("Not found the review ID"));
    }

    public List<Review> getALlReivews(){
        return reviewRepository.findAll();
    }
}
