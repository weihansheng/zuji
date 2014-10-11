package com.project.zuji.tools;

import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.map.Symbol.Color;

public class SymbolTools {
	// 得到绘图样式_多边形_方法
	public static Symbol getSymbol_line(int red, int green, int blue,
			int alpha, int width) {

		Symbol symbol = new Symbol();
		Symbol.Color color = symbol.new Color();
		color.red = red;
		color.green = green;
		color.blue = blue;
		color.alpha = alpha;
		symbol.setLineSymbol(color, width);
		return symbol;
	}

	// 得到绘图样式_多边形_方法
	public static Symbol getSymbol_point(int red, int green, int blue, int alpha) {

		Symbol symbol = new Symbol();
		Symbol.Color color = symbol.new Color();
		color.red = red;
		color.green = green;
		color.blue = blue;
		color.alpha = alpha;
		symbol.setPointSymbol(color);
		return symbol;
	}

	// 得到绘图样式_多边形_方法
	public static Symbol getSymbol_Surface(int red, int green, int blue,
			int alpha, int status, int linewidth) {

		Symbol symbol = new Symbol();
		Symbol.Color color = symbol.new Color();
		color.red = red;
		color.green = green;
		color.blue = blue;
		color.alpha = alpha;
		symbol.setSurface(color, status, linewidth);
		return symbol;
	}

	public static Symbol.Color getSymbolColor(Symbol symbol, int red,
			int green, int blue, int alpha) {
		Symbol.Color color = symbol.new Color();
		color.red = red;
		color.green = green;
		color.blue = blue;
		color.alpha = alpha;
		return color;
	}
}
