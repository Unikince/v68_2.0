/**
 * 
 */
package com.zyhy.lhj_server.game.ajxz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.game.ajxz.poi.template.AjxzOdds;
import com.zyhy.lhj_server.game.gsgl.GsglRollerWeightEnum;
import com.zyhy.lhj_server.game.sbhz.SbhzIconEnum;
import com.zyhy.lhj_server.game.sbhz.poi.template.SbhzOdds;

/**
 * @author ASUS
 * 轮子权重
 */
public enum AjxzRollerWeightEnum {

	A1(1, 1, AjxzIconEnum.S1, 200),A2(1, 1, AjxzIconEnum.A2, 200),A3(1, 1, AjxzIconEnum.J, 200),A4(1, 1, AjxzIconEnum.A1, 200),
	A5(1, 1, AjxzIconEnum.Q, 200),A6(1, 1, AjxzIconEnum.BONUS, 200),A7(1, 1, AjxzIconEnum.T10, 200),A8(1, 1, AjxzIconEnum.A2, 200),
	A9(1, 1, AjxzIconEnum.K, 200),A10(1, 1, AjxzIconEnum.J, 200),A11(1, 1, AjxzIconEnum.A4, 200),A12(1, 1, AjxzIconEnum.A, 200),
	A13(1, 1, AjxzIconEnum.T10, 200),A14(1, 1, AjxzIconEnum.BONUS, 200),A15(1, 1, AjxzIconEnum.K, 200),A16(1, 1, AjxzIconEnum.S1, 200),
	A17(1, 1, AjxzIconEnum.Q, 200),A18(1, 1, AjxzIconEnum.A, 200),A19(1, 1, AjxzIconEnum.K, 200),A20(1, 1, AjxzIconEnum.T10, 200),
	A21(1, 1, AjxzIconEnum.S1, 200),A22(1, 1, AjxzIconEnum.A, 200),A23(1, 1, AjxzIconEnum.J, 200),A24(1, 1, AjxzIconEnum.K, 200),
	A25(1, 1, AjxzIconEnum.T10, 200),A26(1, 1, AjxzIconEnum.A2, 200),A27(1, 1, AjxzIconEnum.Q, 200),A28(1, 1, AjxzIconEnum.A4, 200),
	A29(1, 1, AjxzIconEnum.J, 200),A30(1, 1, AjxzIconEnum.A, 200),A31(1, 1, AjxzIconEnum.T10, 200),A32(1, 1, AjxzIconEnum.A1, 200),
	A33(1, 1, AjxzIconEnum.BONUS, 200),A34(1, 1, AjxzIconEnum.Q, 200),A35(1, 1, AjxzIconEnum.S1, 200),A36(1, 1, AjxzIconEnum.J, 200),
	A37(1, 1, AjxzIconEnum.A, 200),A38(1, 1, AjxzIconEnum.A, 200),A39(1, 1, AjxzIconEnum.T10, 200),A40(1, 1, AjxzIconEnum.A1, 200),
	A41(1, 1, AjxzIconEnum.Q, 200),A42(1, 1, AjxzIconEnum.A3, 200),A43(1, 1, AjxzIconEnum.J, 200),A44(1, 1, AjxzIconEnum.S1, 200),
	A45(1, 1, AjxzIconEnum.BONUS, 200),A46(1, 1, AjxzIconEnum.T10, 200),A47(1, 1, AjxzIconEnum.A2, 200),A48(1, 1, AjxzIconEnum.Q, 200),
	A49(1, 1, AjxzIconEnum.J, 200),A50(1, 1, AjxzIconEnum.A3, 200),A51(1, 1, AjxzIconEnum.A, 200),A52(1, 1, AjxzIconEnum.T10, 200),
	A53(1, 1, AjxzIconEnum.S1, 200),A54(1, 1, AjxzIconEnum.K, 200),A55(1, 1, AjxzIconEnum.A3, 200),A56(1, 1, AjxzIconEnum.J, 200),
	A57(1, 1, AjxzIconEnum.A4, 200),A58(1, 1, AjxzIconEnum.Q, 200),A59(1, 1, AjxzIconEnum.T10, 200),A60(1, 1, AjxzIconEnum.A1, 200),
	A61(1, 1, AjxzIconEnum.BONUS, 200),A62(1, 1, AjxzIconEnum.K, 200),A63(1, 1, AjxzIconEnum.A4, 200),A64(1, 1, AjxzIconEnum.Q, 200),
	A65(1, 1, AjxzIconEnum.A3, 200),A66(1, 1, AjxzIconEnum.BONUS, 200),A67(1, 1, AjxzIconEnum.A4, 200),A68(1, 1, AjxzIconEnum.K, 200),
	A69(1, 1, AjxzIconEnum.A2, 200),A70(1, 1, AjxzIconEnum.A3, 200),A71(1, 1, AjxzIconEnum.S1, 200),A72(1, 1, AjxzIconEnum.A4, 200),
	
	B1(2, 1, AjxzIconEnum.T10, 200),B2(2, 1, AjxzIconEnum.SCATTER, 200),B3(2, 1, AjxzIconEnum.K, 200),B4(2, 1, AjxzIconEnum.Q, 200),
	B5(2, 1, AjxzIconEnum.S1, 200),B6(2, 1, AjxzIconEnum.A, 200),B7(2, 1, AjxzIconEnum.J, 200),B8(2, 1, AjxzIconEnum.BONUS, 200),
	B9(2, 1, AjxzIconEnum.K, 200),B10(2, 1, AjxzIconEnum.Q, 200),B11(2, 1, AjxzIconEnum.A4, 200),B12(2, 1, AjxzIconEnum.T10, 200),
	B13(2, 1, AjxzIconEnum.BONUS, 200),B14(2, 1, AjxzIconEnum.S1, 200),B15(2, 1, AjxzIconEnum.A, 200),B16(2, 1, AjxzIconEnum.Q, 200),
	B17(2, 1, AjxzIconEnum.SCATTER, 200),B18(2, 1, AjxzIconEnum.J, 200),B19(2, 1, AjxzIconEnum.BONUS, 200),B20(2, 1, AjxzIconEnum.K, 200),
	B21(2, 1, AjxzIconEnum.Q, 200),B22(2, 1, AjxzIconEnum.A4, 200),B23(2, 1, AjxzIconEnum.T10, 200),B24(2, 1, AjxzIconEnum.WILD, 200),
	B25(2, 1, AjxzIconEnum.A, 200),B26(2, 1, AjxzIconEnum.K, 200),B27(2, 1, AjxzIconEnum.BONUS, 200),B28(2, 1, AjxzIconEnum.J, 200),
	B29(2, 1, AjxzIconEnum.A4, 200),B30(2, 1, AjxzIconEnum.A, 200),B31(2, 1, AjxzIconEnum.Q, 200),B32(2, 1, AjxzIconEnum.SCATTER, 200),
	B33(2, 1, AjxzIconEnum.T10, 200),B34(2, 1, AjxzIconEnum.WILD, 200),B35(2, 1, AjxzIconEnum.K, 200),B36(2, 1, AjxzIconEnum.BONUS, 200),
	B37(2, 1, AjxzIconEnum.J, 200),B38(2, 1, AjxzIconEnum.A4, 200),B39(2, 1, AjxzIconEnum.S1, 200),B40(2, 1, AjxzIconEnum.Q, 200),
	B41(2, 1, AjxzIconEnum.T10, 200),B42(2, 1, AjxzIconEnum.SCATTER, 200),B43(2, 1, AjxzIconEnum.J, 200),B44(2, 1, AjxzIconEnum.WILD, 200),
	B45(2, 1, AjxzIconEnum.S1, 200),B46(2, 1, AjxzIconEnum.A, 200),B47(2, 1, AjxzIconEnum.A3, 200),B48(2, 1, AjxzIconEnum.J, 200),
	B49(2, 1, AjxzIconEnum.T10, 200),B50(2, 1, AjxzIconEnum.WILD, 200),B51(2, 1, AjxzIconEnum.S1, 200),B52(2, 1, AjxzIconEnum.A3, 200),
	B53(2, 1, AjxzIconEnum.WILD, 200),B54(2, 1, AjxzIconEnum.J, 200),B55(2, 1, AjxzIconEnum.SCATTER, 200),B56(2, 1, AjxzIconEnum.A3, 200),
	B57(2, 1, AjxzIconEnum.Q, 200),B58(2, 1, AjxzIconEnum.A1, 200),B59(2, 1, AjxzIconEnum.WILD, 200),B60(2, 1, AjxzIconEnum.T10, 200),
	B61(2, 1, AjxzIconEnum.SCATTER, 200),B62(2, 1, AjxzIconEnum.K, 200),B63(2, 1, AjxzIconEnum.A2, 200),B64(2, 1, AjxzIconEnum.J, 200),
	B65(2, 1, AjxzIconEnum.A1, 200),B66(2, 1, AjxzIconEnum.A3, 200),B67(2, 1, AjxzIconEnum.A2, 200),B68(2, 1, AjxzIconEnum.T10, 200),
	B69(2, 1, AjxzIconEnum.WILD, 200),B70(2, 1, AjxzIconEnum.A1, 200),B71(2, 1, AjxzIconEnum.A2, 200),B72(2, 1, AjxzIconEnum.SCATTER, 200),
	
	C1(3, 1, AjxzIconEnum.T10, 200),C2(3, 1, AjxzIconEnum.WILD, 200),C3(3, 1, AjxzIconEnum.Q, 200),C4(3, 1, AjxzIconEnum.A2, 200),
	C5(3, 1, AjxzIconEnum.J, 200),C6(3, 1, AjxzIconEnum.S1, 200),C7(3, 1, AjxzIconEnum.K, 200),C8(3, 1, AjxzIconEnum.T10, 200),
	C9(3, 1, AjxzIconEnum.BONUS, 200),C10(3, 1, AjxzIconEnum.A, 200),C11(3, 1, AjxzIconEnum.Q, 200),C12(3, 1, AjxzIconEnum.SCATTER, 200),
	C13(3, 1, AjxzIconEnum.J, 200),C14(3, 1, AjxzIconEnum.S1, 200),C15(3, 1, AjxzIconEnum.K, 200),C16(3, 1, AjxzIconEnum.A4, 200),
	C17(3, 1, AjxzIconEnum.A, 200),C18(3, 1, AjxzIconEnum.T10, 200),C19(3, 1, AjxzIconEnum.WILD, 200),C20(3, 1, AjxzIconEnum.Q, 200),
	C21(3, 1, AjxzIconEnum.A2, 200),C22(3, 1, AjxzIconEnum.A, 200),C23(3, 1, AjxzIconEnum.J, 200),C24(3, 1, AjxzIconEnum.S1, 200),
	C25(3, 1, AjxzIconEnum.K, 200),C26(3, 1, AjxzIconEnum.BONUS, 200),C27(3, 1, AjxzIconEnum.T10, 200),C28(3, 1, AjxzIconEnum.A4, 200),
	C29(3, 1, AjxzIconEnum.A, 200),C30(3, 1, AjxzIconEnum.Q, 200),C31(3, 1, AjxzIconEnum.SCATTER, 200),C32(3, 1, AjxzIconEnum.J, 200),
	C33(3, 1, AjxzIconEnum.A2, 200),C34(3, 1, AjxzIconEnum.A, 200),C35(3, 1, AjxzIconEnum.K, 200),C36(3, 1, AjxzIconEnum.WILD, 200),
	C37(3, 1, AjxzIconEnum.T10, 200),C38(3, 1, AjxzIconEnum.SCATTER, 200),C39(3, 1, AjxzIconEnum.Q, 200),C40(3, 1, AjxzIconEnum.A2, 200),
	C41(3, 1, AjxzIconEnum.J, 200),C42(3, 1, AjxzIconEnum.WILD, 200),C43(3, 1, AjxzIconEnum.K, 200),C44(3, 1, AjxzIconEnum.A4, 200),
	C45(3, 1, AjxzIconEnum.T10, 200),C46(3, 1, AjxzIconEnum.SCATTER, 200),C47(3, 1, AjxzIconEnum.A, 200),C48(3, 1, AjxzIconEnum.WILD, 200),
	C49(3, 1, AjxzIconEnum.J, 200),C50(3, 1, AjxzIconEnum.A3, 200),C51(3, 1, AjxzIconEnum.S1, 200),C52(3, 1, AjxzIconEnum.T10, 200),
	C53(3, 1, AjxzIconEnum.SCATTER, 200),C54(3, 1, AjxzIconEnum.K, 200),C55(3, 1, AjxzIconEnum.WILD, 200),C56(3, 1, AjxzIconEnum.BONUS, 200),
	C57(3, 1, AjxzIconEnum.J, 200),C58(3, 1, AjxzIconEnum.A3, 200),C59(3, 1, AjxzIconEnum.Q, 200),C60(3, 1, AjxzIconEnum.A1, 200),
	C61(3, 1, AjxzIconEnum.S1, 200),C62(3, 1, AjxzIconEnum.T10, 200),C63(3, 1, AjxzIconEnum.WILD, 200),C64(3, 1, AjxzIconEnum.A3, 200),
	C65(3, 1, AjxzIconEnum.BONUS, 200),C66(3, 1, AjxzIconEnum.A1, 200),C67(3, 1, AjxzIconEnum.A4, 200),C68(3, 1, AjxzIconEnum.Q, 200),
	C69(3, 1, AjxzIconEnum.SCATTER, 200),C70(3, 1, AjxzIconEnum.A3, 200),C71(3, 1, AjxzIconEnum.BONUS, 200),C72(3, 1, AjxzIconEnum.A1, 200),
	
	D1(4, 1, AjxzIconEnum.T10, 200),D2(4, 1, AjxzIconEnum.Q, 200),D3(4, 1, AjxzIconEnum.A1, 200),D4(4, 1, AjxzIconEnum.J, 200),
	D5(4, 1, AjxzIconEnum.SCATTER, 200),D6(4, 1, AjxzIconEnum.K, 200),D7(4, 1, AjxzIconEnum.Q, 200),D8(4, 1, AjxzIconEnum.S1, 200),
	D9(4, 1, AjxzIconEnum.T10, 200),D10(4, 1, AjxzIconEnum.SCATTER, 200),D11(4, 1, AjxzIconEnum.K, 200),D12(4, 1, AjxzIconEnum.Q, 200),
	D13(4, 1, AjxzIconEnum.A1, 200),D14(4, 1, AjxzIconEnum.J, 200),D15(4, 1, AjxzIconEnum.S1, 200),D16(4, 1, AjxzIconEnum.K, 200),
	D17(4, 1, AjxzIconEnum.T10, 200),D18(4, 1, AjxzIconEnum.WILD, 200),D19(4, 1, AjxzIconEnum.Q, 200),D20(4, 1, AjxzIconEnum.A1, 200),
	D21(4, 1, AjxzIconEnum.K, 200),D22(4, 1, AjxzIconEnum.J, 200),D23(4, 1, AjxzIconEnum.WILD, 200),D24(4, 1, AjxzIconEnum.A, 200),
	D25(4, 1, AjxzIconEnum.Q, 200),D26(4, 1, AjxzIconEnum.A1, 200),D27(4, 1, AjxzIconEnum.K, 200),D28(4, 1, AjxzIconEnum.T10, 200),
	D29(4, 1, AjxzIconEnum.SCATTER, 200),D30(4, 1, AjxzIconEnum.A, 200),D31(4, 1, AjxzIconEnum.WILD, 200),D32(4, 1, AjxzIconEnum.J, 200),
	D33(4, 1, AjxzIconEnum.SCATTER, 200),D34(4, 1, AjxzIconEnum.Q, 200),D35(4, 1, AjxzIconEnum.A, 200),D36(4, 1, AjxzIconEnum.K, 200),
	D37(4, 1, AjxzIconEnum.T10, 200),D38(4, 1, AjxzIconEnum.SCATTER, 200),D39(4, 1, AjxzIconEnum.A, 200),D40(4, 1, AjxzIconEnum.WILD, 200),
	D41(4, 1, AjxzIconEnum.J, 200),D42(4, 1, AjxzIconEnum.SCATTER, 200),D43(4, 1, AjxzIconEnum.A, 200),D44(4, 1, AjxzIconEnum.A2, 200),
	D45(4, 1, AjxzIconEnum.T10, 200),D46(4, 1, AjxzIconEnum.WILD, 200),D47(4, 1, AjxzIconEnum.S1, 200),D48(4, 1, AjxzIconEnum.A3, 200),
	D49(4, 1, AjxzIconEnum.J, 200),D50(4, 1, AjxzIconEnum.SCATTER, 200),D51(4, 1, AjxzIconEnum.S1, 200),D52(4, 1, AjxzIconEnum.A2, 200),
	D53(4, 1, AjxzIconEnum.T10, 200),D54(4, 1, AjxzIconEnum.A4, 200),D55(4, 1, AjxzIconEnum.BONUS, 200),D56(4, 1, AjxzIconEnum.S1, 200),
	D57(4, 1, AjxzIconEnum.WILD, 200),D58(4, 1, AjxzIconEnum.A3, 200),D59(4, 1, AjxzIconEnum.J, 200),D60(4, 1, AjxzIconEnum.A4, 200),
	D61(4, 1, AjxzIconEnum.BONUS, 200),D62(4, 1, AjxzIconEnum.A2, 200),D63(4, 1, AjxzIconEnum.T10, 200),D64(4, 1, AjxzIconEnum.BONUS, 200),
	D65(4, 1, AjxzIconEnum.A4, 200),D66(4, 1, AjxzIconEnum.A3, 200),D67(4, 1, AjxzIconEnum.WILD, 200),D68(4, 1, AjxzIconEnum.BONUS, 200),
	D69(4, 1, AjxzIconEnum.A2, 200),D70(4, 1, AjxzIconEnum.A4, 200),D71(4, 1, AjxzIconEnum.A3, 200),D72(4, 1, AjxzIconEnum.BONUS, 200),
	
	E1(5, 1, AjxzIconEnum.T10, 200),E2(5, 1, AjxzIconEnum.A2, 200),E3(5, 1, AjxzIconEnum.K, 200),E4(5, 1, AjxzIconEnum.A4, 200),
	E5(5, 1, AjxzIconEnum.J, 200),E6(5, 1, AjxzIconEnum.BONUS, 200),E7(5, 1, AjxzIconEnum.Q, 200),E8(5, 1, AjxzIconEnum.A1, 200),
	E9(5, 1, AjxzIconEnum.A, 200),E10(5, 1, AjxzIconEnum.T10, 200),E11(5, 1, AjxzIconEnum.BONUS, 200),E12(5, 1, AjxzIconEnum.K, 200),
	E13(5, 1, AjxzIconEnum.A2, 200),E14(5, 1, AjxzIconEnum.J, 200),E15(5, 1, AjxzIconEnum.S1, 200),E16(5, 1, AjxzIconEnum.A, 200),
	E17(5, 1, AjxzIconEnum.Q, 200),E18(5, 1, AjxzIconEnum.BONUS, 200),E19(5, 1, AjxzIconEnum.T10, 200),E20(5, 1, AjxzIconEnum.A4, 200),
	E21(5, 1, AjxzIconEnum.K, 200),E22(5, 1, AjxzIconEnum.S1, 200),E23(5, 1, AjxzIconEnum.J, 200),E24(5, 1, AjxzIconEnum.BONUS, 200),
	E25(5, 1, AjxzIconEnum.Q, 200),E26(5, 1, AjxzIconEnum.A1, 200),E27(5, 1, AjxzIconEnum.K, 200),E28(5, 1, AjxzIconEnum.T10, 200),
	E29(5, 1, AjxzIconEnum.S1, 200),E30(5, 1, AjxzIconEnum.A, 200),E31(5, 1, AjxzIconEnum.K, 200),E32(5, 1, AjxzIconEnum.J, 200),
	E33(5, 1, AjxzIconEnum.WILD, 200),E34(5, 1, AjxzIconEnum.Q, 200),E35(5, 1, AjxzIconEnum.A2, 200),E36(5, 1, AjxzIconEnum.T10, 200),
	E37(5, 1, AjxzIconEnum.BONUS, 200),E38(5, 1, AjxzIconEnum.K, 200),E39(5, 1, AjxzIconEnum.WILD, 200),E40(5, 1, AjxzIconEnum.Q, 200),
	E41(5, 1, AjxzIconEnum.J, 200),E42(5, 1, AjxzIconEnum.S1, 200),E43(5, 1, AjxzIconEnum.A, 200),E44(5, 1, AjxzIconEnum.Q, 200),
	E45(5, 1, AjxzIconEnum.WILD, 200),E46(5, 1, AjxzIconEnum.T10, 200),E47(5, 1, AjxzIconEnum.A3, 200),E48(5, 1, AjxzIconEnum.K, 200),
	E49(5, 1, AjxzIconEnum.J, 200),E50(5, 1, AjxzIconEnum.S1, 200),E51(5, 1, AjxzIconEnum.Q, 200),E52(5, 1, AjxzIconEnum.A1, 200),
	E53(5, 1, AjxzIconEnum.A, 200),E54(5, 1, AjxzIconEnum.T10, 200),E55(5, 1, AjxzIconEnum.WILD, 200),E56(5, 1, AjxzIconEnum.A3, 200),
	E57(5, 1, AjxzIconEnum.A, 200),E58(5, 1, AjxzIconEnum.J, 200),E59(5, 1, AjxzIconEnum.WILD, 200),E60(5, 1, AjxzIconEnum.A4, 200),
	E61(5, 1, AjxzIconEnum.A, 200),E62(5, 1, AjxzIconEnum.T10, 200),E63(5, 1, AjxzIconEnum.A3, 200),E64(5, 1, AjxzIconEnum.S1, 200),
	E65(5, 1, AjxzIconEnum.Q, 200),E66(5, 1, AjxzIconEnum.A4, 200),E67(5, 1, AjxzIconEnum.J, 200),E68(5, 1, AjxzIconEnum.WILD, 200),
	E69(5, 1, AjxzIconEnum.A3, 200),E70(5, 1, AjxzIconEnum.T10, 200),E71(5, 1, AjxzIconEnum.A4, 200),E72(5, 1, AjxzIconEnum.WILD, 200),
	
	/*A1(1, 0, AjxzIconEnum.A1, 150),
	A2(1, 1, AjxzIconEnum.A2, 200),
	A3(1, 2, AjxzIconEnum.A3, 200),
	A4(1, 3, AjxzIconEnum.A4, 300),
	A5(1, 4, AjxzIconEnum.BONUS, 300),
	A6(1, 5, AjxzIconEnum.S1, 300),
	A7(1, 6, AjxzIconEnum.A, 300),
	A8(1, 7, AjxzIconEnum.K, 300),
	A9(1, 8, AjxzIconEnum.Q, 300),
	A10(1, 9, AjxzIconEnum.J, 300),
	A11(1, 10, AjxzIconEnum.T10, 300),
	A12(1, 11, AjxzIconEnum.A1, 200),
	A13(1, 12, AjxzIconEnum.A2, 200),
	A14(1, 13, AjxzIconEnum.A3, 200),
	A15(1, 14, AjxzIconEnum.A4, 300),
	A16(1, 15, AjxzIconEnum.S1, 300),
	A17(1, 16, AjxzIconEnum.A, 300),
	A18(1, 17, AjxzIconEnum.K, 300),
	A19(1, 18, AjxzIconEnum.Q, 300),
	A20(1, 19, AjxzIconEnum.J, 300),
	A21(1, 20, AjxzIconEnum.T10, 300),
	
	B1(2, 0, AjxzIconEnum.A1, 150),
	B2(2, 1, AjxzIconEnum.A2, 200),
	B3(2, 2, AjxzIconEnum.A3, 200),
	B4(2, 3, AjxzIconEnum.A4, 300),
	B5(2, 4, AjxzIconEnum.BONUS, 300),
	B6(2, 5, AjxzIconEnum.S1, 300),
	B7(2, 6, AjxzIconEnum.A, 300),
	B8(2, 7, AjxzIconEnum.K, 300),
	B9(2, 8, AjxzIconEnum.Q, 300),
	B10(2, 9, AjxzIconEnum.J, 300),
	B11(2, 10, AjxzIconEnum.T10, 300),
	B12(2, 11, AjxzIconEnum.WILD, 100),
	B13(2, 12, AjxzIconEnum.SCATTER, 1000),
	B14(2, 13, AjxzIconEnum.A1, 200),
	B15(2, 14, AjxzIconEnum.A2, 200),
	B16(2, 15, AjxzIconEnum.A3, 200),
	B17(2, 16, AjxzIconEnum.A4, 300),
	B18(2, 17, AjxzIconEnum.S1, 300),
	B19(2, 18, AjxzIconEnum.A, 300),
	B20(2, 19, AjxzIconEnum.K, 300),
	B21(2, 20, AjxzIconEnum.Q, 300),
	B22(2, 21, AjxzIconEnum.J, 300),
	B23(2, 22, AjxzIconEnum.T10, 300),
	
	B24(2, 12, AjxzIconEnum.SCATTER, 500),
	B25(2, 12, AjxzIconEnum.SCATTER, 500),
	B26(2, 12, AjxzIconEnum.SCATTER, 500),
	B27(2, 12, AjxzIconEnum.SCATTER, 500),
	B28(2, 12, AjxzIconEnum.SCATTER, 500),
	B29(2, 12, AjxzIconEnum.SCATTER, 500),
	
	
	C1(3, 0, AjxzIconEnum.A1, 150),
	C2(3, 1, AjxzIconEnum.A2, 200),
	C3(3, 2, AjxzIconEnum.A3, 200),
	C4(3, 3, AjxzIconEnum.A4, 300),
	C5(3, 4, AjxzIconEnum.BONUS, 300),
	C6(3, 5, AjxzIconEnum.S1, 300),
	C7(3, 6, AjxzIconEnum.A, 300),
	C8(3, 7, AjxzIconEnum.K, 300),
	C9(3, 8, AjxzIconEnum.Q, 300),
	C10(3, 9, AjxzIconEnum.J, 300),
	C11(3, 10, AjxzIconEnum.T10, 300),
	C12(3, 11, AjxzIconEnum.WILD, 100),
	C13(3, 12, AjxzIconEnum.SCATTER, 1000),
	C14(3, 13, AjxzIconEnum.A1, 200),
	C15(3, 14, AjxzIconEnum.A2, 200),
	C16(3, 15, AjxzIconEnum.A3, 200),
	C17(3, 16, AjxzIconEnum.A4, 300),
	C18(3, 17, AjxzIconEnum.S1, 300),
	C19(3, 18, AjxzIconEnum.A, 300),
	C20(3, 19, AjxzIconEnum.K, 300),
	C21(3, 20, AjxzIconEnum.Q, 300),
	C22(3, 21, AjxzIconEnum.J, 300),
	C23(3, 22, AjxzIconEnum.T10, 300),
	
	C24(3, 12, AjxzIconEnum.SCATTER, 500),
	C25(3, 12, AjxzIconEnum.SCATTER, 500),
	C26(3, 12, AjxzIconEnum.SCATTER, 500),
	C27(3, 12, AjxzIconEnum.SCATTER, 500),
	C28(3, 12, AjxzIconEnum.SCATTER, 500),
	C29(3, 12, AjxzIconEnum.SCATTER, 500),
	
	D1(4, 0, AjxzIconEnum.A1, 150),
	D2(4, 1, AjxzIconEnum.A2, 200),
	D3(4, 2, AjxzIconEnum.A3, 200),
	D4(4, 3, AjxzIconEnum.A4, 300),
	D5(4, 4, AjxzIconEnum.BONUS, 300),
	D6(4, 5, AjxzIconEnum.S1, 300),
	D7(4, 6, AjxzIconEnum.A, 300),
	D8(4, 7, AjxzIconEnum.K, 300),
	D9(4, 8, AjxzIconEnum.Q, 300),
	D10(4, 9, AjxzIconEnum.J, 300),
	D11(4, 10, AjxzIconEnum.T10, 300),
	D12(4, 11, AjxzIconEnum.WILD, 100),
	D13(4, 12, AjxzIconEnum.SCATTER, 1000),
	D14(4, 13, AjxzIconEnum.A1, 200),
	D15(4, 14, AjxzIconEnum.A2, 200),
	D16(4, 15, AjxzIconEnum.A3, 200),
	D17(4, 16, AjxzIconEnum.A4, 300),
	D18(4, 18, AjxzIconEnum.S1, 300),
	D19(4, 19, AjxzIconEnum.A, 300),
	D20(4, 20, AjxzIconEnum.K, 300),
	D21(4, 21, AjxzIconEnum.Q, 300),
	D22(4, 22, AjxzIconEnum.J, 300),
	D23(4, 23, AjxzIconEnum.T10, 300),
	
	D24(4, 12, AjxzIconEnum.SCATTER, 500),
	D25(4, 12, AjxzIconEnum.SCATTER, 500),
	D26(4, 12, AjxzIconEnum.SCATTER, 500),
	D27(4, 12, AjxzIconEnum.SCATTER, 500),
	D28(4, 12, AjxzIconEnum.SCATTER, 500),
	D29(4, 12, AjxzIconEnum.SCATTER, 500),
	
	E1(5, 0, AjxzIconEnum.A1, 150),
	E2(5, 1, AjxzIconEnum.A2, 200),
	E3(5, 2, AjxzIconEnum.A3, 200),
	E4(5, 3, AjxzIconEnum.A4, 300),
	E5(5, 4, AjxzIconEnum.BONUS, 300),
	E6(5, 5, AjxzIconEnum.S1, 300),
	E7(5, 6, AjxzIconEnum.A, 300),
	E8(5, 7, AjxzIconEnum.K, 300),
	E9(5, 8, AjxzIconEnum.Q, 300),
	E10(5, 9, AjxzIconEnum.J, 300),
	E11(5, 10, AjxzIconEnum.T10, 300),
	E12(5, 11, AjxzIconEnum.WILD, 100),
	E14(5, 12, AjxzIconEnum.A1, 200),
	E15(5, 13, AjxzIconEnum.A2, 200),
	E16(5, 14, AjxzIconEnum.A3, 200),
	E17(5, 15, AjxzIconEnum.A4, 300),
	E18(5, 16, AjxzIconEnum.S1, 300),
	E19(5, 17, AjxzIconEnum.A, 300),
	E20(5, 18, AjxzIconEnum.K, 300),
	E21(5, 19, AjxzIconEnum.Q, 300),
	E22(5, 20, AjxzIconEnum.J, 300),
	E23(5, 21, AjxzIconEnum.T10, 300),*/
	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private AjxzIconEnum icon;
	//权重
	private int weight;
	
	private AjxzRollerWeightEnum(int id, int index, AjxzIconEnum icon, int weight){
		this.id = id;
		this.index = index;
		this.icon = icon;
		this.weight = weight;
	}

	public int getId() {
		return id;
	}

	public int getIndex() {
		return index;
	}

	public AjxzIconEnum getIcon() {
		return icon;
	}

	public int getWeight() {
		return weight;
	}

	/**
	 * 按指定轴取图标
	 * @param i
	 * @return
	 */
	public static Collection<? extends Window> windowInfo(int c, List<AjxzOdds> all) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<AjxzOdds> col = new ArrayList<>();
		List<AjxzIconEnum> icons = new ArrayList<>();
		// 取出一列的图标
		for (AjxzOdds odds : all) {
			if(odds.getCol() == c){
				col.add(odds);
			}
		}
		//System.out.println("col" + col);
		for (AjxzOdds odds : col) {
			AjxzIconEnum icon = null;
			for (AjxzIconEnum e : AjxzIconEnum.values()) {
				if (e.getName().equalsIgnoreCase(odds.getName())) {
					icon = e;
				}
			}
			icons.add(icon);
		}
		//System.out.println("icons" + icons);
		
		// 根据随机数取图标
		int random = RandomUtil.getRandom(0, icons.size() - 1);
		for (int i = 1; i <= 3; i++) {
			AjxzIconEnum icon = icons.get(random);
			ws.add(new WindowInfo(c, i, icon));
			random ++ ;
			if (random > (icons.size() - 1)) {
				random = 0;
			}
		}
		//System.out.println("本次取到的图标:" + ws);
		return ws;
	}
	/**
	 * 按指定轴和位置取图标
	 * @param 
	 * @return
	 */
	public static WindowInfo windowInfo(int c,int d, List<AjxzOdds> all) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<AjxzOdds> col = new ArrayList<>();
		List<AjxzIconEnum> icons = new ArrayList<>();
		// 取出一列的图标
		for (AjxzOdds odds : all) {
			if(odds.getCol() == c){
				col.add(odds);
			}
		}
		//System.out.println("col" + col);
		for (AjxzOdds odds : col) {
			AjxzIconEnum icon = null;
			for (AjxzIconEnum e : AjxzIconEnum.values()) {
				if (e.getName().equalsIgnoreCase(odds.getName())) {
					icon = e;
				}
			}
			icons.add(icon);
		}
		//System.out.println("icons" + icons);
		
		int random = RandomUtil.getRandom(0, icons.size() - 1);
		AjxzIconEnum icon = icons.get(random);
		ws.add(new WindowInfo(c, d, icon));
		return ws.get(0);
	}
}
