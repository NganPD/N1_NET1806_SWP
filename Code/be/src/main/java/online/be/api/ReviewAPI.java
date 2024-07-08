package online.be.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.be.entity.Review;
import online.be.model.Request.VenueReviewRequest;
import online.be.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reivews")
@SecurityRequirement(name = "api")
public class ReviewAPI {
    @Autowired
    private ReviewService reviewService;


    @GetMapping("/{id}")
    public ResponseEntity getReviewById(@PathVariable long reviewId){
        Review review = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }

    @PostMapping("/create")
    public ResponseEntity createReview(@RequestBody VenueReviewRequest request){
        Review review = reviewService.sendReview(request);
        return ResponseEntity.ok(review);
    }

    @GetMapping
    public ResponseEntity getAllReviews(){
        return ResponseEntity.ok(reviewService.getALlReivews());
    }

    @GetMapping
    public ResponseEntity getTopRatedVenues(){
        return ResponseEntity.ok(reviewService.getTopRatedVenues());
    }

    @GetMapping
    public ResponseEntity getMostBookedVenues(){
        return ResponseEntity.ok(reviewService.findMostBookedVenues());
    }
}
