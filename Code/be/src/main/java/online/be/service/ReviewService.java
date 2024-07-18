
package online.be.service;

import online.be.entity.Account;
import online.be.entity.Review;
import online.be.entity.Venue;
import online.be.exception.BadRequestException;
import online.be.model.Request.ReviewRequest;
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
    AuthenticationService authenticationService;

    @Autowired
    VenueRepository venueRepository;


    public Review sendReview(ReviewRequest request){
        //lay thong tin nguoi dung hien tai
        Account user = authenticationService.getCurrentAccount();
        //tim venue dua tren id
        Venue venue= venueRepository.findVenueById(request.getVenueId());
        //kiem tra danh gia co nam trong 1 va 5 khong
        if(request.getRating() < 1 || request.getRating() > 5){
            throw new BadRequestException("Rating must be between 1 and 5");
        }

        //tao mot object review moi
        Review review = new Review();
        review.setComment(request.getFeedback());
        review.setRating(request.getRating());
        review.setCreatedDate(LocalDateTime.now());
        review.setAccount(user);
        review.setVenue(venue);

        //lưu vào repository
        return reviewRepository.save(review);
    }

    public Review getReviewById(long reviewId){
        return reviewRepository.findById(reviewId)
                .orElseThrow(()-> new BadRequestException("Not found the review ID"));
    }

    public List<Review> getALlReivews(){
        return reviewRepository.findAll();
    }

    public List<Venue> getTopRatedVenues(){
        return reviewRepository.findTopRatedVenues();
    }

}
