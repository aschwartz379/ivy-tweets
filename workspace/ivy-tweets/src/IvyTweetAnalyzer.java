import java.util.ArrayList;

public class IvyTweetAnalyzer {
	static ArrayList<Tweet> tweets;
	static ArrayList<Campus> campuses;

	public static void main(String[] args) {
		TweetParser tweetReader = new TweetParser();
		tweets = tweetReader.readTweets("300k_tweets.txt");
		LocationReader locations = new LocationReader();
		campuses = locations.setUpCampuses("campuses.csv");
		setUpTweetCampuses();
		retrieveCampusTweets("Penn");
	}

	static void setUpTweetCampuses() {
		// find closest campus to tweet latitude and longitude
		Campus closestCampus = null;
		for (Tweet t : tweets) {
			double distance = Double.MAX_VALUE;
			for (Campus c : campuses) {
				double trialDistance = Math.sqrt(Math.pow(t.latitude
						- c.latitude, 2)
						+ Math.pow(t.longitude - c.longitude, 2));
				if (trialDistance < distance) {
					distance = trialDistance;
					closestCampus = c;
				}
			}
			if (closestCampus != null) {
				// if tweet is within one mile of a campus center, include it
				if (distance <= 0.01449275362) {
					// add state information to tweet object
					t.Campus = closestCampus;
					closestCampus.addTweetToCampus(t);
					// add tweet count to campus
					closestCampus.numberOfTweets++;
				}
				// otherwise do nothing
			}
		}
	}

	public static void retrieveCampusTweets(String campusName) {
		for (Campus c : campuses) {
			if (c.name.equals(campusName)) {
				for (Tweet t : c.campusTweets) {
					System.out.println(t.content);
				}
			}
		}
	}

}
