package dev.simplesolution.chrome;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList; 


public class ButtonAdapter extends ArrayAdapter<String> {
    private OnButtonClickListener listener;
    private int memo;
    public interface OnButtonClickListener {
        void onButtonClick(int position, String item, int memo);
    }

    public ButtonAdapter(Context context, List<String> data, OnButtonClickListener listener, int memo) {
        super(context, R.layout.item_button, data);
        this.listener = listener;
        this.memo = memo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                .inflate(R.layout.item_button, parent, false);
        }

        String item = getItem(position);
        Button button = convertView.findViewById(R.id.list_button);
        button.setText(item);

        // 设置点击事件
        button.setOnClickListener(v -> {
            if (listener != null) {
                listener.onButtonClick(position, item, memo);
            }
        });
        Toast.makeText(MainActivity.this, "getView被调用", Toast.LENGTH_SHORT).show();
        return convertView;
    }
}