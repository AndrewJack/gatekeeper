package technology.mainthread.apps.gatekeeper.view.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RemoteViews;

import javax.inject.Inject;

import technology.mainthread.apps.gatekeeper.R;
import technology.mainthread.apps.gatekeeper.model.event.GatekeeperState;
import timber.log.Timber;

public class WidgetViewModel {

    private final Context context;
    private final Resources resources;
    private final AppWidgetManager appWidgetManager;

    @Inject
    public WidgetViewModel(Context context, Resources resources, AppWidgetManager appWidgetManager) {
        this.context = context;
        this.resources = resources;
        this.appWidgetManager = appWidgetManager;
    }

    public void refreshWidget(@NonNull int[] widgetIds) {
        Timber.d("refresh widget");
        ComponentName thisWidget = new ComponentName(context, PrimeWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.action_widget);
            // Set the text
            remoteViews.setTextViewText(R.id.btn_widget_action, resources.getString(R.string.btn_title_prime));

            Intent intent = PrimeWidgetService.getButtonPressedIntent(context, allWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.btn_widget_action, pendingIntent);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    public void updateWidgetState(@GatekeeperState String state) {
        switch (state) {
            case GatekeeperState.ONLINE:
                break;
            case GatekeeperState.OFFLINE:
                break;
            case GatekeeperState.PRIMED:
                break;
            default:
                break;
        }
    }

    private void showProgress(@NonNull int[] widgetIds) {
        Timber.d("showProgress");
        for (int widgetId : widgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.action_widget);
            remoteViews.setViewVisibility(R.id.progress, View.VISIBLE);
            remoteViews.setTextViewText(R.id.btn_widget_action, "Priming...");

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    private void hideProgress(@NonNull int[] widgetIds) {
        for (int widgetId : widgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.action_widget);
            remoteViews.setViewVisibility(R.id.progress, View.GONE);
            remoteViews.setTextViewText(R.id.btn_widget_action, resources.getString(R.string.btn_title_prime));

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
