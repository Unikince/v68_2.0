/**
 * 
 */
package com.zyhy.lhj_server.game.swk.poi.template;

import org.apache.poi.ss.usermodel.Row;

import com.zyhy.lhj_server.game.swk.poi.impl.BaseXlsTemplateBuilder;
import com.zyhy.lhj_server.game.swk.poi.impl.TemplateObject;



/**
 * @author linanjun
 *
 */
public class SwkOddsBuilder implements BaseXlsTemplateBuilder {

	@Override
	public TemplateObject buildTemplate(Row row) {
		SwkOdds f = new SwkOdds();
		f.setId((int)row.getCell(0).getNumericCellValue());
		f.setCol((int)row.getCell(1).getNumericCellValue());
		f.setName(row.getCell(2).getStringCellValue());
		f.setType((int)row.getCell(3).getNumericCellValue());
		return f;
	}

}
