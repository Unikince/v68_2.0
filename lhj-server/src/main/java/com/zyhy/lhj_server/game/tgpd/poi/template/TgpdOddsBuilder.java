/**
 * 
 */
package com.zyhy.lhj_server.game.tgpd.poi.template;

import org.apache.poi.ss.usermodel.Row;

import com.zyhy.lhj_server.game.tgpd.poi.impl.BaseXlsTemplateBuilder;
import com.zyhy.lhj_server.game.tgpd.poi.impl.TemplateObject;




/**
 * @author linanjun
 *
 */
public class TgpdOddsBuilder implements BaseXlsTemplateBuilder {

	@Override
	public TemplateObject buildTemplate(Row row) {
		TgpdOdds f = new TgpdOdds();
		f.setId((int)row.getCell(0).getNumericCellValue());
		f.setCol((int)row.getCell(1).getNumericCellValue());
		f.setName(row.getCell(2).getStringCellValue());
		f.setType((int)row.getCell(3).getNumericCellValue());
		f.setWeight((int)row.getCell(4).getNumericCellValue());
		return f;
	}

}
