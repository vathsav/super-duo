package barqsoft.footballscores.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;

public class ScoresWidgetIntentService extends IntentService {

    public Uri BASE_CONTENT_URI = Uri.parse("content://barqsoft.footballscores");

    public ScoresWidgetIntentService() {
        super("ScoresWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                ScoresWidget.class));



        Cursor cursor = getContentResolver().query(
                BASE_CONTENT_URI,
                new String[]{
                        DatabaseContract.scores_table.AWAY_COL,
                        DatabaseContract.scores_table.AWAY_GOALS_COL,
                        DatabaseContract.scores_table.HOME_COL,
                        DatabaseContract.scores_table.HOME_GOALS_COL,
                },
                null,
                null,
                null
        );

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.scores_widget);

            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widgetBody, pendingIntent);

            if (cursor != null)
                if (cursor.moveToFirst()) {
                    views.setTextViewText(R.id.widgetAwayTeam, cursor.getString(0));
                    views.setTextViewText(R.id.widgetAwayScore, cursor.getString(1));
                    views.setTextViewText(R.id.widgetHomeTeam, cursor.getString(2));
                    views.setTextViewText(R.id.widgetHomeScore, cursor.getString(3));
                }

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        if (cursor != null)
            cursor.close();
    }
}