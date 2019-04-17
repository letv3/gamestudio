package sk.tuke.gamestudio.server.webservice;


import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/rating")
public class RatingServiceRest {

    @Autowired
    private RatingService ratingService;

    //http://localhost:8080/rest/rating
    @POST
    @Consumes("application/json")
    public Response setRating(Rating rating){
        ratingService.setRating(rating);
        return Response.ok().build();
    }

    //http://localhost:8080/rest/rating/sudoku
    @GET
    @Path("/{game}")
    @Produces("application/json")
    public int  getAvarageRating(@PathParam("game")String game){
        return ratingService.getAverageRating(game);
    }

    //http://localhost:8080/rest/rating/sudoku/letv
    @GET
    @Path("/{game}/{player}")
    @Produces("application/json")
    public int  getRating(@PathParam("game")String game,@PathParam("player") String player){
        return ratingService.getRating(game,player);
    }




}
