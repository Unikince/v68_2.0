/**
 * 
 */
package com.zyhy.lhj_server.game.xywjs.poi.template;

import org.apache.poi.ss.usermodel.Row;

import com.zyhy.lhj_server.game.xywjs.poi.impl.BaseXlsTemplateBuilder;
import com.zyhy.lhj_server.game.xywjs.poi.impl.TemplateObject;

/**
 * @author linanjun
 *
 */
public class FruitOddsBuilder implements BaseXlsTemplateBuilder {

	@Override
	public TemplateObject buildTemplate(Row row) {
		FruitOdds f = new FruitOdds();
		f.setId((int)row.getCell(0).getNumericCellValue());
		f.setName(row.getCell(1).getStringCellValue());
		f.setMultiple((int)row.getCell(2).getNumericCellValue());
		f.setOdds((int)row.getCell(3).getNumericCellValue());
		f.setSign((int)row.getCell(4).getNumericCellValue());
		f.setBet((int)row.getCell(5).getNumericCellValue());
		f.setType((int)row.getCell(6).getNumericCellValue());
		return f;
	}

}
