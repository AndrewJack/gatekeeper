package technology.mainthread.apps.gatekeeper.view;

import android.content.Context;
import android.support.wearable.view.GridPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import technology.mainthread.apps.gatekeeper.R;

public class ActionGridPagerAdapter extends GridPagerAdapter {

    private static final String[] ACTION = new String[]{
            GatekeeperAction.ACTION_PRIME,
            GatekeeperAction.ACTION_UNLOCK
    };

    private final LayoutInflater inflator;
    private final ActionClickListener listener;

    public ActionGridPagerAdapter(Context context, ActionClickListener listener) {
        this.inflator = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public int getRowCount() {
        return ACTION.length;
    }

    @Override
    public int getColumnCount(int i) {
        return 1;
    }

    @Override
    public View instantiateItem(ViewGroup viewGroup, int row, int column) {
        View view = inflator.inflate(R.layout.item_action, viewGroup, false);
        Button btnAction = (Button) view.findViewById(R.id.txt_action);
        btnAction.setText(ACTION[row]);
        btnAction.setOnClickListener(v -> {
            listener.onItemClicked(ACTION[row]);
        });
        viewGroup.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int row, int column, Object view) {
        viewGroup.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view.equals(o);
    }

    public interface ActionClickListener {
        void onItemClicked(String action);
    }
}
