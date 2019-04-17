package sk.tuke.gamestudio.service;

import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Transactional
public class RatingServiceJPA implements RatingService {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        entityManager.merge(rating);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {

        int r=(int) Math.round((double)entityManager.createQuery("SELECT AVG(e.rating) FROM Rating e WHERE e.game=:game")
                .setParameter("game",game).getSingleResult());
        return r;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        Rating current = (Rating) entityManager.createNamedQuery("Rating.getRating").setParameter("game", game)
                .setParameter("player", player).getSingleResult();

        return current.getRating();
    }
}
