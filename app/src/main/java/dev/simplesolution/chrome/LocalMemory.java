package dev.simplesolution.chrome;
import java.util.Arrays;
public class LocalMemory {
    // 历史记录数组，固定长度15
    private String[] history;
    // 当前记录数量
    private int count;
    
    public LocalMemory() {
        history = new String[15]; // 初始化长度为15的数组
        count = 0; // 初始记录数为0
    }
    
    /**
     * 添加新历史记录
     * @param newRecord 新记录内容
     */
    public void addRecord(String newRecord) {
        for (int i = 0; i < history.length; i++) {
            if(history[i] == newRecord){
                return;
            }            
        }
        // 如果数组已满，先向前移动所有记录
        if (count == history.length) {
            // 将所有元素向前移动一位
            for (int i = 0; i < history.length - 1; i++) {
                history[i] = history[i + 1];
            }
        } else {
            count++; // 记录数增加
        }
        
        // 将新记录添加到最后一个位置
        history[count - 1] = newRecord;
    }
    
    /**
     * 获取所有历史记录
     * @return 包含所有记录的数组（可能包含null）
     */
    public String[] getAllRecords() {
        return Arrays.copyOf(history, count);
    }
    
    /**
     * 获取最近N条记录
     * @param n 要获取的记录数
     * @return 包含最近N条记录的数组
     */
    public String[] getRecentRecords(int n) {
        n = Math.min(n, count); // 不能超过现有记录数
        String[] result = new String[n];
        System.arraycopy(history, count - n, result, 0, n);
        return result;
    }
    
    /**
     * 清空历史记录
     */
    public void clear() {
        Arrays.fill(history, null);
        count = 0;
    }
    
    /**
     * 获取当前记录数量
     * @return 记录数量
     */
    public int getCount() {
        return count;
    }
}