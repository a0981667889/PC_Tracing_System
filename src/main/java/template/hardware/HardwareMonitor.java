package template.hardware;

import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Components;
import com.profesorfalken.jsensors.model.components.Gpu;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.Sensors;
import template.model.HardwareStats;

public class HardwareMonitor {
    // 初始化 OSHI 系統資訊
    private final SystemInfo si = new SystemInfo();
    private final CentralProcessor processor = si.getHardware().getProcessor();
    private final Sensors sensors = si.getHardware().getSensors();

    // 用於計算 CPU 使用率的舊刻度
    private long[] oldTicks = new long[CentralProcessor.TickType.values().length];

    public HardwareStats getStats() {
        // 1. 使用 OSHI 抓取 CPU 溫度與使用率 (對 14 代 CPU 支援較好)
        double cpuTemp = sensors.getCpuTemperature(); // 取得 CPU 溫度
        double cpuUsage = processor.getSystemCpuLoadBetweenTicks(oldTicks) * 100; // 取得使用率
        oldTicks = processor.getSystemCpuLoadTicks(); // 更新刻度

        // 2. 使用 JSensors 抓取 GPU 數據 (OSHI 對 GPU 支援較弱)
        Components components = JSensors.get.components();
        double gpuUsage = 0.0;
        double gpuTemp = 0.0;

        if (components.gpus != null && !components.gpus.isEmpty()) {
            Gpu gpu = components.gpus.get(0);
            if (!gpu.sensors.loads.isEmpty()) {
                gpuUsage = gpu.sensors.loads.get(0).value;
            }
            if (!gpu.sensors.temperatures.isEmpty()) {
                gpuTemp = gpu.sensors.temperatures.get(0).value;
            }
        }

        // 3. 備案：如果 OSHI 抓不到溫度 (回傳 0)，再嘗試用 JSensors 抓一次
        if (cpuTemp <= 0 && components.cpus != null && !components.cpus.isEmpty()) {
            var jsCpu = components.cpus.get(0);
            if (!jsCpu.sensors.temperatures.isEmpty()) {
                cpuTemp = jsCpu.sensors.temperatures.get(0).value;
            }
        }

        return new HardwareStats(cpuUsage, gpuUsage, cpuTemp, gpuTemp);
    }
}