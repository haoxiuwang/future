package dev.simplesolution.chrome;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;
import dev.simplesolution.chrome.LocalMemory;
import java.net.URL;
import java.net.MalformedURLException;


public class ButtonAdapter extends BaseAdapter {
    private OnButtonClickListener listener;
    private int memo;
    private final LocalMemory memory;
    private Context context;
    public interface OnButtonClickListener {
        void onButtonClick(int position, String item, int memo);
    }

    public ButtonAdapter(Context context, LocalMemory memory, OnButtonClickListener listener, int memo) {
        this.listener = listener;
        this.memo = memo;
        this.memory = memory;
        this.context = context;
    }
     @Override
    public int getCount() {
        return memory.getCount(); // 实时数量
    }

    @Override
    public String getItem(int position) {
        return memory.getRawArrayForAdapter()[position]; // 直接访问原数组
    }
    @Override
    public long getItemId(int position) {
        return position; // 如果没有唯一ID，直接返回position
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                .inflate(R.layout.item_button, parent, false);
        }

        String item = getItem(position);
        Button button = convertView.findViewById(R.id.list_button);
        
        try {
            URL url = new URL(item);
            button.setText(url.getHost());
            // 设置点击事件
            button.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onButtonClick(position, item, memo);
                }
            });
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        
        
        
        return convertView;
    }
}