package org.appjam.bongbaek.domain.event.service.util;

import org.appjam.bongbaek.domain.event.service.util.vo.RangeInfo;

public class RangeCalculator {
	public static RangeInfo calculateRange(int cost) {
		if (cost <= 30_000) {
			return new RangeInfo(10_000, 50_000);
		}
		if (cost <= 100_000) {
			return new RangeInfo(cost - 20_000, cost + 20_000);
		}
		if (cost <= 300_000) {
			return new RangeInfo(cost - 30_000, cost + 30_000);
		}
		return new RangeInfo(cost - 50_000, cost + 50_000);
	}
}
