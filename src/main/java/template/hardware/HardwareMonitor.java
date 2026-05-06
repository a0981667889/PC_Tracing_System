package template.hardware;

import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Components;
import oshi.SystemInfo;
import template.model.HardwareStats;

public class HardwareMonitor {
    private final SystemInfo si = new SystemInfo();
    private long[] oldTicks = new long[oshi.hardware.CentralProcessor.TickType.values().length];

    public HardwareStats getStats() {
        var hardware = si.getHardware();
        double cpuTemp = hardware.getSensors().getCpuTemperature();
        double cpuUsage = hardware.getProcessor().getSystemCpuLoadBetweenTicks(oldTicks) * 100;
        oldTicks = hardware.getProcessor().getSystemCpuLoadTicks();

        Components components = JSensors.get.components();
        double gpuUsage = 0.0, gpuTemp = 0.0;
        String name = "Unknown GPU";

        if (components.gpus != null && !components.gpus.isEmpty()) {
            var gpu = components.gpus.get(0);
            name = gpu.name;
            if (!gpu.sensors.loads.isEmpty()) gpuUsage = gpu.sensors.loads.get(0).value;
            if (!gpu.sensors.temperatures.isEmpty()) gpuTemp = gpu.sensors.temperatures.get(0).value;
        }

        // 避免遞迴呼叫，直接傳入取得的數據[cite: 3]
        return new HardwareStats(cpuUsage, gpuUsage, cpuTemp, gpuTemp, name);
    }
}