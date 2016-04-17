package technology.mainthread.apps.gatekeeper.view.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

public class PrimeWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] widgetIds) {
        context.startService(PrimeWidgetService.getRefreshWidgetsIntent(context, widgetIds));
    }

}
