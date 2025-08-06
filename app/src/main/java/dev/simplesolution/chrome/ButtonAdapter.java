package dev.simplesolution.chrome;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;



public class ButtonAdapter extends BaseAdapter {
    private OnButtonClickListener listener;
    private int memo;
    private final LocalMemory memory;
    public interface OnButtonClickListener {
        void onButtonClick(int position, String item, int memo);
    }

    public ButtonAdapter(LocalMemory memory, OnButtonClickListener listener, int memo) {
        this.listener = listener;
        this.memo = memo;
        this.memory = memory;
    }
     @Override
    public int getCount() {
        return memory.getCount(); // 实时数量
    }

    @Override
    public String getItem(int position) {
        return memory.getHistoryArrayForAdapter()[position]; // 直接访问原数组
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Toast.makeText(getContext(), "getView被调用开始", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getContext(), "getView被调用", Toast.LENGTH_SHORT).show();
        return convertView;
    }
}