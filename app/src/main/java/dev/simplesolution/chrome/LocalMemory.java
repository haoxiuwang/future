package dev.simplesolution.chrome;

import java.util.Arrays;
import java.util.Objects;

/**
 * 固定容量的历史记录存储器（线程不安全，需外部同步）
 */
public class LocalMemory {
    private final String[] history;  // 核心数组
    private int count;               // 实际记录数
    private static final int MAX_SIZE = 15;

    public LocalMemory() {
        history = new String[MAX_SIZE];
        count = 0;
    }

    /**
     * 添加新记录（自动去重，满时淘汰最旧记录）
     * @param newRecord 非空记录内容
     */
    public void addRecord(String newRecord) {
        if (newRecord == null || newRecord.trim().isEmpty()) {
            return;
        }

        // 检查重复（避免相同记录重复存储）
        for (int i = 0; i < count; i++) {
            if (newRecord.equals(history[i])) {
                // 如果记录已存在，将其移到最新位置（可选行为）
                moveRecordToLatest(i);
                return;
            }
        }

        // 数组已满时，移除最旧记录（下标0）
        if (count == MAX_SIZE) {
            System.arraycopy(history, 1, history, 0, MAX_SIZE - 1);
            count--;
        }

        // 添加新记录到末尾
        history[count] = newRecord;
        count++;
    }

    // 将已存在的记录移动到最新位置（内部方法）
    private void moveRecordToLatest(int existingIndex) {
        String existingRecord = history[existingIndex];
        System.arraycopy(history, existingIndex + 1, history, existingIndex, count - existingIndex - 1);
        history[count - 1] = existingRecord;
    }

    /**
     * 获取当前记录数量
     */
    public int getCount() {
        return count;
    }

    /**
     * 获取指定位置的记录（供Adapter的getItem()调用）
     * @throws IndexOutOfBoundsException 如果位置无效
     */
    public String getRecordAt(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        return history[index];
    }

    /**
     * 返回原始数组的直接引用（仅供Adapter使用！外部禁止修改！）
     */
    public String[] getRawArrayForAdapter() {
        return history;
    }

    /**
     * 获取最近N条记录（安全的副本）
     */
    public String[] getRecentRecords(int n) {
        n = Math.min(Math.max(n, 0), count);  // 确保n在[0, count]范围内
        return Arrays.copyOfRange(history, count - n, count);
    }

    /**
     * 清空所有记录
     */
    public void clear() {
        Arrays.fill(history, null);
        count = 0;
    }

    @Override
    public String toString() {
        return Arrays.toString(getRecentRecords(count));
    }
}