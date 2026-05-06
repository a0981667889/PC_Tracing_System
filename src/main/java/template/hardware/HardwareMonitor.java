package template.hardware;

import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Components;
import com.profesorfalken.jsensors.model.components.Gpu;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.Sensors;
import template.model.HardwareStats;

public class HardwareMonitor {
    private final SystemInfo si = new SystemInfo();
    private final CentralProcessor processor = si.getHardware().getProcessor();
    private final Sensors sensors = si.getHardware().getSensors();
    private long[] oldTicks = new long[CentralProcessor.TickType.values().length];

    public HardwareStats getStats() {
        // 1. 抓取 CPU 數據 (OSHI)
        double cpuTemp = sensors.getCpuTemperature();
        double cpuUsage = processor.getSystemCpuLoadBetweenTicks(oldTicks) * 100;
        oldTicks = processor.getSystemCpuLoadTicks();

        // 2. 抓取 GPU 數據 (JSensors)
        // 在這裡定義 components，確保下方都能使用到它
        Components components = JSensors.get.components();
        double gpuUsage = 0.0;
        double gpuTemp = 0.0;
        String gpuName = "Unknown GPU";

        if (components.gpus != null && !components.gpus.isEmpty()) {
            Gpu gpu = components.gpus.get(0);
            gpuName = gpu.name; // 取得 GPU 名稱
            if (!gpu.sensors.loads.isEmpty()) {
                gpuUsage = gpu.sensors.loads.get(0).value;
            }
            if (!gpu.sensors.temperatures.isEmpty()) {
                gpuTemp = gpu.sensors.temperatures.get(0).value;
            }
        }

        // 3. 備案：如果 CPU 溫度抓不到
        if (cpuTemp <= 0 && components.cpus != null && !components.cpus.isEmpty()) {
            var jsCpu = components.cpus.get(0);
            if (!jsCpu.sensors.temperatures.isEmpty()) {
                cpuTemp = jsCpu.sensors.temperatures.get(0).value;
            }
        }

        // --- 修正重點 ---
        // 直接傳入剛才取得的 gpuName，不要再呼叫 getStats()
        return new HardwareStats(cpuUsage, gpuUsage, cpuTemp, gpuTemp, gpuName);
    }
}