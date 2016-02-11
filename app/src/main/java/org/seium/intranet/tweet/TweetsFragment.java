package org.seium.intranet.tweet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import org.seium.intranet.R;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.cards.material.MaterialLargeImageCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TweetsFragment extends Fragment {
    private static String TAG = TweetsFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private CardArrayRecyclerViewAdapter mAdapter;
    private RecyclerViewMaterialAdapter mRecyclerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new CardArrayRecyclerViewAdapter(getContext(), new ArrayList<Card>());
        mAdapter.setRowLayoutId(R.layout.card_tweet);
        mRecyclerAdapter = new RecyclerViewMaterialAdapter(mAdapter);
    }

    private void updateCards(List<Card> cards) {
        mAdapter.setCards(cards);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setItemViewCacheSize(cards.size());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.cards, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.cards_crv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setHasFixedSize(false);

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        new FetchTweets().execute(16687379);
    }

    private class FetchTweets extends AsyncTask<Integer, Void, List<Card>> {
        private Twitter mTwitter;

        protected void onPreExecute() {
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey("Hw6I5s0mPv6jW8iVlP3j4duXM")
                    .setOAuthConsumerSecret("cpvNcE24hvJAMqCQFyQxiiQI7I5aTe0xsA9d1na6uvzhhLOYp5")
                    .setOAuthAccessToken("743193763-19KrEO847XSSkigwJwthF53QNNUKTwmvsXzB0PPp")
                    .setOAuthAccessTokenSecret("YS3fbEdQ7rWEkjCHbWrFHHyjU3BPk92iJBAF3wnCHod2t");

            TwitterFactory tf = new TwitterFactory(cb.build());
            mTwitter = tf.getInstance();
        }


        @Override
        protected List<Card> doInBackground(Integer... ids) {
            Paging paging = new Paging(1, 10);
            List<Card> cards = new ArrayList<>();

            try {
                ResponseList<twitter4j.Status> tweets = mTwitter.getUserTimeline(ids[0], paging);

                for (twitter4j.Status tweet : tweets) {
                    MaterialLargeImageCard.SetupWizard builder = MaterialLargeImageCard.with
                            (getActivity());

                    if (tweet.getRetweetedStatus() != null) {
                        tweet = tweet.getRetweetedStatus();
                    }

                    builder.setTitle(tweet.getUser().getName());
                    builder.setSubTitle(tweet.getText());

                    MediaEntity[] mediaEntities = tweet.getMediaEntities();
                    if (mediaEntities.length > 0) {
                        builder.useDrawableUrl(mediaEntities[0].getMediaURL());
                    } else {
                        builder.useDrawableUrl(tweet.getUser().getProfileBannerURL());
                    }

                    MaterialLargeImageCard card = builder.build();
                    cards.add(card);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return cards;
        }

        @Override
        protected void onPostExecute(List<Card> cards) {
            updateCards(cards);
        }
    }
}
