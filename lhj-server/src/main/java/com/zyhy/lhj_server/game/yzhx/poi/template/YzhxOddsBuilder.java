/**
 * 
 */
package com.zyhy.lhj_server.game.yzhx.poi.template;

import org.apache.poi.ss.usermodel.Row;

import com.zyhy.lhj_server.game.yzhx.poi.impl.BaseXlsTemplateBuilder;
import com.zyhy.lhj_server.game.yzhx.poi.impl.TemplateObject;


/**
 * @author linanjun
 *
 */
public class YzhxOddsBuilder implements BaseXlsTemplateBuilder {

	@Override
	public TemplateObject buildTemplate(Row row) {
		YzhxOdds f = new YzhxOdds();
		f.setId((int)row.getCell(0).getNumericCellValue());
		f.setCol((int)row.getCell(1).getNumericCellValue());
		f.setName(row.getCell(2).getStringCellValue());
		f.setType((int)row.getCell(3).getNumericCellValue());
		return f;
	}

}
