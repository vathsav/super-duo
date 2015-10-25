package barqsoft.footballscores.widget;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;

public class ScoresDetailedWidgetIntentService extends RemoteViewsService {

    public Uri BASE_CONTENT_URI = Uri.parse("content://barqsoft.footballscores");

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            Cursor cursor = null;

            @Override
            public void onCreate() {
            }

            @Override
            public void onDataSetChanged() {
                if (cursor != null)
                    cursor.close();

                final long identityToken = Binder.clearCallingIdentity();

                String selection = "date = ?";
//                String[] selectionArgs = {"10"};

                cursor = getContentResolver().query(
                        BASE_CONTENT_URI,
                        new String[]{
                                DatabaseContract.scores_table.DATE_COL,
                                DatabaseContract.scores_table.AWAY_COL,
                                DatabaseContract.scores_table.AWAY_GOALS_COL,
                                DatabaseContract.scores_table.HOME_COL,
                                DatabaseContract.scores_table.HOME_GOALS_COL,
                        },
                        selection,
                        null,
                        null
                );
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
            }

            @Override
            public int getCount() {
                if (cursor == null)
                    return 0;
                else
                    return cursor.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        cursor == null || !cursor.moveToPosition(position)) {
                    return null;
                }

                cursor.moveToPosition(position);

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.scores_detailed_widget_list_item);
                views.setTextViewText(R.id.widgetAwayTeam, cursor.getString(1));
                // Away Goals
                if (cursor.getString(2).equals("-1"))
                    views.setTextViewText(R.id.widgetAwayScore, "-");
                else
                    views.setTextViewText(R.id.widgetAwayScore, cursor.getString(2));
                views.setTextViewText(R.id.widgetHomeTeam, cursor.getString(3));
                // Home Goals
                if (cursor.getString(2).equals("-1"))
                    views.setTextViewText(R.id.widgetHomeScore, "-");
                else
                    views.setTextViewText(R.id.widgetHomeScore, cursor.getString(4));

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.scores_detailed_widget_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
    }
}