/**
 * 
 */
package com.zyhy.lhj_server.game.zctz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.util.RandomUtil;

/**
 * @author ASUS
 * 轮子权重
 */
public enum ZctzRollerWeightEnum_backup {
	
	A1(1, 1, ZctzIconEnum.T11, 200),A2(1, 1, ZctzIconEnum.T10, 200),A3(1, 1, ZctzIconEnum.K, 200),A4(1, 1, ZctzIconEnum.J, 200),
	A5(1, 1, ZctzIconEnum.A3, 200),A6(1, 1, ZctzIconEnum.Q, 200),A7(1, 1, ZctzIconEnum.K, 200),A8(1, 1, ZctzIconEnum.T11, 200),
	A9(1, 1, ZctzIconEnum.T10, 200),A10(1, 1, ZctzIconEnum.A1, 200),A11(1, 1, ZctzIconEnum.A3, 200),A12(1, 1, ZctzIconEnum.J, 200),
	A13(1, 1, ZctzIconEnum.K, 200),A14(1, 1, ZctzIconEnum.T11, 200),A15(1, 1, ZctzIconEnum.T10, 200),A16(1, 1, ZctzIconEnum.A1, 200),
	A17(1, 1, ZctzIconEnum.A3, 200),A18(1, 1, ZctzIconEnum.K, 200),A19(1, 1, ZctzIconEnum.Q, 200),A20(1, 1, ZctzIconEnum.J, 200),
	A21(1, 1, ZctzIconEnum.T11, 200),A22(1, 1, ZctzIconEnum.T10, 200),A23(1, 1, ZctzIconEnum.J, 200),A24(1, 1, ZctzIconEnum.A3, 200),
	A25(1, 1, ZctzIconEnum.K, 200),A26(1, 1, ZctzIconEnum.J, 200),A27(1, 1, ZctzIconEnum.T11, 200),A28(1, 1, ZctzIconEnum.T10, 200),
	A29(1, 1, ZctzIconEnum.SCATTER, 200),A30(1, 1, ZctzIconEnum.A3, 200),A31(1, 1, ZctzIconEnum.K, 200),A32(1, 1, ZctzIconEnum.J, 200),
	A33(1, 1, ZctzIconEnum.T11, 200),A34(1, 1, ZctzIconEnum.T10, 200),A35(1, 1, ZctzIconEnum.S1, 200),A36(1, 1, ZctzIconEnum.J, 200),
	A37(1, 1, ZctzIconEnum.A1, 200),A38(1, 1, ZctzIconEnum.SCATTER, 200),A39(1, 1, ZctzIconEnum.Q, 200),A40(1, 1, ZctzIconEnum.T11, 200),
	A41(1, 1, ZctzIconEnum.T10, 200),A42(1, 1, ZctzIconEnum.Q, 200),A43(1, 1, ZctzIconEnum.J, 200),A44(1, 1, ZctzIconEnum.K, 200),
	A45(1, 1, ZctzIconEnum.A, 200),A46(1, 1, ZctzIconEnum.Q, 200),A47(1, 1, ZctzIconEnum.T11, 200),A48(1, 1, ZctzIconEnum.T10, 200),
	A49(1, 1, ZctzIconEnum.A2, 200),A50(1, 1, ZctzIconEnum.J, 200),A51(1, 1, ZctzIconEnum.A, 200),A52(1, 1, ZctzIconEnum.Q, 200),
	A53(1, 1, ZctzIconEnum.A2, 200),A54(1, 1, ZctzIconEnum.T11, 200),A55(1, 1, ZctzIconEnum.T10, 200),A56(1, 1, ZctzIconEnum.A, 200),
	A57(1, 1, ZctzIconEnum.Q, 200),A58(1, 1, ZctzIconEnum.J, 200),A59(1, 1, ZctzIconEnum.A2, 200),A60(1, 1, ZctzIconEnum.Q, 200),
	A61(1, 1, ZctzIconEnum.A, 200),A62(1, 1, ZctzIconEnum.T11, 200),A63(1, 1, ZctzIconEnum.T10, 200),A64(1, 1, ZctzIconEnum.S1, 200),
	A65(1, 1, ZctzIconEnum.A, 200),A66(1, 1, ZctzIconEnum.Q, 200),A67(1, 1, ZctzIconEnum.T10, 200),A68(1, 1, ZctzIconEnum.T11, 200),
	A69(1, 1, ZctzIconEnum.A2, 200),A70(1, 1, ZctzIconEnum.A, 200),A71(1, 1, ZctzIconEnum.SCATTER, 200),A72(1, 1, ZctzIconEnum.T11, 200),
	
	B1(2, 1, ZctzIconEnum.T11, 200),B2(2, 1, ZctzIconEnum.T10, 200),B3(2, 1, ZctzIconEnum.J, 200),B4(2, 1, ZctzIconEnum.K, 200),
	B5(2, 1, ZctzIconEnum.A, 200),B6(2, 1, ZctzIconEnum.A3, 200),B7(2, 1, ZctzIconEnum.J, 200),B8(2, 1, ZctzIconEnum.T11, 200),
	B9(2, 1, ZctzIconEnum.T10, 200),B10(2, 1, ZctzIconEnum.Q, 200),B11(2, 1, ZctzIconEnum.K, 200),B12(2, 1, ZctzIconEnum.A3, 200),
	B13(2, 1, ZctzIconEnum.T11, 200),B14(2, 1, ZctzIconEnum.J, 200),B15(2, 1, ZctzIconEnum.T10, 200),B16(2, 1, ZctzIconEnum.S1, 200),
	B17(2, 1, ZctzIconEnum.K, 200),B18(2, 1, ZctzIconEnum.T11, 200),B19(2, 1, ZctzIconEnum.A, 200),B20(2, 1, ZctzIconEnum.A3, 200),
	B21(2, 1, ZctzIconEnum.T10, 200),B22(2, 1, ZctzIconEnum.Q, 200),B23(2, 1, ZctzIconEnum.K, 200),B24(2, 1, ZctzIconEnum.J, 200),
	B25(2, 1, ZctzIconEnum.T11, 200),B26(2, 1, ZctzIconEnum.SCATTER, 200),B27(2, 1, ZctzIconEnum.K, 200),B28(2, 1, ZctzIconEnum.T10, 200),
	B29(2, 1, ZctzIconEnum.J, 200),B30(2, 1, ZctzIconEnum.K, 200),B31(2, 1, ZctzIconEnum.T11, 200),B32(2, 1, ZctzIconEnum.S1, 200),
	B33(2, 1, ZctzIconEnum.A3, 200),B34(2, 1, ZctzIconEnum.T10, 200),B35(2, 1, ZctzIconEnum.J, 200),B36(2, 1, ZctzIconEnum.T11, 200),
	B37(2, 1, ZctzIconEnum.Q, 200),B38(2, 1, ZctzIconEnum.K, 200),B39(2, 1, ZctzIconEnum.T10, 200),B40(2, 1, ZctzIconEnum.SCATTER, 200),
	B41(2, 1, ZctzIconEnum.A1, 200),B42(2, 1, ZctzIconEnum.T11, 200),B43(2, 1, ZctzIconEnum.J, 200),B44(2, 1, ZctzIconEnum.A1, 200),
	B45(2, 1, ZctzIconEnum.K, 200),B46(2, 1, ZctzIconEnum.A2, 200),B47(2, 1, ZctzIconEnum.T10, 200),B48(2, 1, ZctzIconEnum.A, 200),
	B49(2, 1, ZctzIconEnum.A3, 200),B50(2, 1, ZctzIconEnum.T11, 200),B51(2, 1, ZctzIconEnum.A2, 200),B52(2, 1, ZctzIconEnum.J, 200),
	B53(2, 1, ZctzIconEnum.A1, 200),B54(2, 1, ZctzIconEnum.T10, 200),B55(2, 1, ZctzIconEnum.SCATTER, 200),B56(2, 1, ZctzIconEnum.J, 200),
	B57(2, 1, ZctzIconEnum.A, 200),B58(2, 1, ZctzIconEnum.Q, 200),B59(2, 1, ZctzIconEnum.T11, 200),B60(2, 1, ZctzIconEnum.WILD, 200),
	B61(2, 1, ZctzIconEnum.Q, 200),B62(2, 1, ZctzIconEnum.A, 200),B63(2, 1, ZctzIconEnum.T10, 200),B64(2, 1, ZctzIconEnum.A2, 200),
	B65(2, 1, ZctzIconEnum.Q, 200),B66(2, 1, ZctzIconEnum.A, 200),B67(2, 1, ZctzIconEnum.SCATTER, 200),B68(2, 1, ZctzIconEnum.T11, 200),
	B69(2, 1, ZctzIconEnum.Q, 200),B70(2, 1, ZctzIconEnum.A2, 200),B71(2, 1, ZctzIconEnum.A, 200),B72(2, 1, ZctzIconEnum.Q, 200),
	
	C1(3, 1, ZctzIconEnum.T11, 200),C2(3, 1, ZctzIconEnum.A2, 200),C3(3, 1, ZctzIconEnum.T10, 200),C4(3, 1, ZctzIconEnum.SCATTER, 200),
	C5(3, 1, ZctzIconEnum.A3, 200),C6(3, 1, ZctzIconEnum.K, 200),C7(3, 1, ZctzIconEnum.T11, 200),C8(3, 1, ZctzIconEnum.T10, 200),
	C9(3, 1, ZctzIconEnum.Q, 200),C10(3, 1, ZctzIconEnum.A2, 200),C11(3, 1, ZctzIconEnum.K, 200),C12(3, 1, ZctzIconEnum.S1, 200),
	C13(3, 1, ZctzIconEnum.T10, 200),C14(3, 1, ZctzIconEnum.A3, 200),C15(3, 1, ZctzIconEnum.T11, 200),C16(3, 1, ZctzIconEnum.A, 200),
	C17(3, 1, ZctzIconEnum.K, 200),C18(3, 1, ZctzIconEnum.A2, 200),C19(3, 1, ZctzIconEnum.T10, 200),C20(3, 1, ZctzIconEnum.SCATTER, 200),
	C21(3, 1, ZctzIconEnum.A3, 200),C22(3, 1, ZctzIconEnum.K, 200),C23(3, 1, ZctzIconEnum.T11, 200),C24(3, 1, ZctzIconEnum.Q, 200),
	C25(3, 1, ZctzIconEnum.T10, 200),C26(3, 1, ZctzIconEnum.S1, 200),C27(3, 1, ZctzIconEnum.A3, 200),C28(3, 1, ZctzIconEnum.K, 200),
	C29(3, 1, ZctzIconEnum.WILD, 200),C30(3, 1, ZctzIconEnum.Q, 200),C31(3, 1, ZctzIconEnum.A3, 200),C32(3, 1, ZctzIconEnum.T10, 200),
	C33(3, 1, ZctzIconEnum.T11, 200),C34(3, 1, ZctzIconEnum.A, 200),C35(3, 1, ZctzIconEnum.K, 200),C36(3, 1, ZctzIconEnum.A2, 200),
	C37(3, 1, ZctzIconEnum.J, 200),C38(3, 1, ZctzIconEnum.A3, 200),C39(3, 1, ZctzIconEnum.T10, 200),C40(3, 1, ZctzIconEnum.T11, 200),
	C41(3, 1, ZctzIconEnum.Q, 200),C42(3, 1, ZctzIconEnum.K, 200),C43(3, 1, ZctzIconEnum.S1, 200),C44(3, 1, ZctzIconEnum.J, 200),
	C45(3, 1, ZctzIconEnum.A1, 200),C46(3, 1, ZctzIconEnum.K, 200),C47(3, 1, ZctzIconEnum.T11, 200),C48(3, 1, ZctzIconEnum.J, 200),
	C49(3, 1, ZctzIconEnum.A1, 200),C50(3, 1, ZctzIconEnum.T10, 200),C51(3, 1, ZctzIconEnum.A, 200),C52(3, 1, ZctzIconEnum.WILD, 200),
	C53(3, 1, ZctzIconEnum.J, 200),C54(3, 1, ZctzIconEnum.A1, 200),C55(3, 1, ZctzIconEnum.T11, 200),C56(3, 1, ZctzIconEnum.Q, 200),
	C57(3, 1, ZctzIconEnum.T10, 200),C58(3, 1, ZctzIconEnum.J, 200),C59(3, 1, ZctzIconEnum.SCATTER, 200),C60(3, 1, ZctzIconEnum.A, 200),
	C61(3, 1, ZctzIconEnum.Q, 200),C62(3, 1, ZctzIconEnum.J, 200),C63(3, 1, ZctzIconEnum.T11, 200),C64(3, 1, ZctzIconEnum.J, 200),
	C65(3, 1, ZctzIconEnum.A, 200),C66(3, 1, ZctzIconEnum.Q, 200),C67(3, 1, ZctzIconEnum.J, 200),C68(3, 1, ZctzIconEnum.A, 200),
	C69(3, 1, ZctzIconEnum.Q, 200),C70(3, 1, ZctzIconEnum.T11, 200),C71(3, 1, ZctzIconEnum.A, 200),C72(3, 1, ZctzIconEnum.J, 200),
	
	D1(4, 1, ZctzIconEnum.T11, 200),D2(4, 1, ZctzIconEnum.T10, 200),D3(4, 1, ZctzIconEnum.A3, 200),D4(4, 1, ZctzIconEnum.SCATTER, 200),
	D5(4, 1, ZctzIconEnum.J, 200),D6(4, 1, ZctzIconEnum.Q, 200),D7(4, 1, ZctzIconEnum.K, 200),D8(4, 1, ZctzIconEnum.T11, 200),
	D9(4, 1, ZctzIconEnum.T10, 200),D10(4, 1, ZctzIconEnum.Q, 200),D11(4, 1, ZctzIconEnum.A3, 200),D12(4, 1, ZctzIconEnum.SCATTER, 200),
	D13(4, 1, ZctzIconEnum.K, 200),D14(4, 1, ZctzIconEnum.T11, 200),D15(4, 1, ZctzIconEnum.T10, 200),D16(4, 1, ZctzIconEnum.J, 200),
	D17(4, 1, ZctzIconEnum.A1, 200),D18(4, 1, ZctzIconEnum.S1, 200),D19(4, 1, ZctzIconEnum.A3, 200),D20(4, 1, ZctzIconEnum.J, 200),
	D21(4, 1, ZctzIconEnum.K, 200),D22(4, 1, ZctzIconEnum.T11, 200),D23(4, 1, ZctzIconEnum.T10, 200),D24(4, 1, ZctzIconEnum.Q, 200),
	D25(4, 1, ZctzIconEnum.A1, 200),D26(4, 1, ZctzIconEnum.J, 200),D27(4, 1, ZctzIconEnum.A3, 200),D28(4, 1, ZctzIconEnum.WILD, 200),
	D29(4, 1, ZctzIconEnum.Q, 200),D30(4, 1, ZctzIconEnum.T11, 200),D31(4, 1, ZctzIconEnum.T10, 200),D32(4, 1, ZctzIconEnum.K, 200),
	D33(4, 1, ZctzIconEnum.A, 200),D34(4, 1, ZctzIconEnum.J, 200),D35(4, 1, ZctzIconEnum.Q, 200),D36(4, 1, ZctzIconEnum.K, 200),
	D37(4, 1, ZctzIconEnum.T11, 200),D38(4, 1, ZctzIconEnum.T10, 200),D39(4, 1, ZctzIconEnum.SCATTER, 200),D40(4, 1, ZctzIconEnum.J, 200),
	D41(4, 1, ZctzIconEnum.A3, 200),D42(4, 1, ZctzIconEnum.A, 200),D43(4, 1, ZctzIconEnum.K, 200),D44(4, 1, ZctzIconEnum.T11, 200),
	D45(4, 1, ZctzIconEnum.T10, 200),D46(4, 1, ZctzIconEnum.A2, 200),D47(4, 1, ZctzIconEnum.J, 200),D48(4, 1, ZctzIconEnum.A, 200),
	D49(4, 1, ZctzIconEnum.A1, 200),D50(4, 1, ZctzIconEnum.SCATTER, 200),D51(4, 1, ZctzIconEnum.K, 200),D52(4, 1, ZctzIconEnum.T11, 200),
	D53(4, 1, ZctzIconEnum.T10, 200),D54(4, 1, ZctzIconEnum.Q, 200),D55(4, 1, ZctzIconEnum.A, 200),D56(4, 1, ZctzIconEnum.J, 200),
	D57(4, 1, ZctzIconEnum.A2, 200),D58(4, 1, ZctzIconEnum.Q, 200),D59(4, 1, ZctzIconEnum.A, 200),D60(4, 1, ZctzIconEnum.T11, 200),
	D61(4, 1, ZctzIconEnum.T10, 200),D62(4, 1, ZctzIconEnum.K, 200),D63(4, 1, ZctzIconEnum.A2, 200),D64(4, 1, ZctzIconEnum.Q, 200),
	D65(4, 1, ZctzIconEnum.J, 200),D66(4, 1, ZctzIconEnum.A, 200),D67(4, 1, ZctzIconEnum.T10, 200),D68(4, 1, ZctzIconEnum.T11, 200),
	D69(4, 1, ZctzIconEnum.A2, 200),D70(4, 1, ZctzIconEnum.A, 200),D71(4, 1, ZctzIconEnum.S1, 200),D72(4, 1, ZctzIconEnum.Q, 200),
	
	E1(5, 1, ZctzIconEnum.T11, 200),E2(5, 1, ZctzIconEnum.T10, 200),E3(5, 1, ZctzIconEnum.J, 200),E4(5, 1, ZctzIconEnum.K, 200),
	E5(5, 1, ZctzIconEnum.A1, 200),E6(5, 1, ZctzIconEnum.A3, 200),E7(5, 1, ZctzIconEnum.T11, 200),E8(5, 1, ZctzIconEnum.T10, 200),
	E9(5, 1, ZctzIconEnum.J, 200),E10(5, 1, ZctzIconEnum.A3, 200),E11(5, 1, ZctzIconEnum.K, 200),E12(5, 1, ZctzIconEnum.A1, 200),
	E13(5, 1, ZctzIconEnum.A2, 200),E14(5, 1, ZctzIconEnum.T10, 200),E15(5, 1, ZctzIconEnum.T11, 200),E16(5, 1, ZctzIconEnum.J, 200),
	E17(5, 1, ZctzIconEnum.A3, 200),E18(5, 1, ZctzIconEnum.K, 200),E19(5, 1, ZctzIconEnum.S1, 200),E20(5, 1, ZctzIconEnum.A1, 200),
	E21(5, 1, ZctzIconEnum.T10, 200),E22(5, 1, ZctzIconEnum.T11, 200),E23(5, 1, ZctzIconEnum.A2, 200),E24(5, 1, ZctzIconEnum.A3, 200),
	E25(5, 1, ZctzIconEnum.K, 200),E26(5, 1, ZctzIconEnum.A, 200),E27(5, 1, ZctzIconEnum.A1, 200),E28(5, 1, ZctzIconEnum.T10, 200),
	E29(5, 1, ZctzIconEnum.T11, 200),E30(5, 1, ZctzIconEnum.J, 200),E31(5, 1, ZctzIconEnum.Q, 200),E32(5, 1, ZctzIconEnum.K, 200),
	E33(5, 1, ZctzIconEnum.A2, 200),E34(5, 1, ZctzIconEnum.A3, 200),E35(5, 1, ZctzIconEnum.Q, 200),E36(5, 1, ZctzIconEnum.K, 200),
	E37(5, 1, ZctzIconEnum.T11, 200),E38(5, 1, ZctzIconEnum.T10, 200),E39(5, 1, ZctzIconEnum.Q, 200),E40(5, 1, ZctzIconEnum.J, 200),
	E41(5, 1, ZctzIconEnum.SCATTER, 200),E42(5, 1, ZctzIconEnum.K, 200),E43(5, 1, ZctzIconEnum.Q, 200),E44(5, 1, ZctzIconEnum.J, 200),
	E45(5, 1, ZctzIconEnum.T10, 200),E46(5, 1, ZctzIconEnum.T11, 200),E47(5, 1, ZctzIconEnum.A, 200),E48(5, 1, ZctzIconEnum.J, 200),
	E49(5, 1, ZctzIconEnum.A3, 200),E50(5, 1, ZctzIconEnum.Q, 200),E51(5, 1, ZctzIconEnum.A, 200),E52(5, 1, ZctzIconEnum.K, 200),
	E53(5, 1, ZctzIconEnum.Q, 200),E54(5, 1, ZctzIconEnum.S1, 200),E55(5, 1, ZctzIconEnum.A, 200),E56(5, 1, ZctzIconEnum.Q, 200),
	E57(5, 1, ZctzIconEnum.T10, 200),E58(5, 1, ZctzIconEnum.T11, 200),E59(5, 1, ZctzIconEnum.SCATTER, 200),E60(5, 1, ZctzIconEnum.J, 200),
	E61(5, 1, ZctzIconEnum.A, 200),E62(5, 1, ZctzIconEnum.Q, 200),E63(5, 1, ZctzIconEnum.T10, 200),E64(5, 1, ZctzIconEnum.T11, 200),
	E65(5, 1, ZctzIconEnum.A2, 200),E66(5, 1, ZctzIconEnum.A, 200),E67(5, 1, ZctzIconEnum.Q, 200),E68(5, 1, ZctzIconEnum.SCATTER, 200),
	E69(5, 1, ZctzIconEnum.T11, 200),E70(5, 1, ZctzIconEnum.T10, 200),E71(5, 1, ZctzIconEnum.A2, 200),E72(5, 1, ZctzIconEnum.A, 200),
	
	
	/*A1(1, 0, ZctzIconEnum.S1, 1),
	A2(1, 1, ZctzIconEnum.A1, 2),
	A3(1, 2, ZctzIconEnum.A2, 3),
	A4(1, 3, ZctzIconEnum.A3, 4),
	A5(1, 4, ZctzIconEnum.A, 60),
	A6(1, 5, ZctzIconEnum.K, 80),
	A7(1, 6, ZctzIconEnum.Q, 100),
	A8(1, 7, ZctzIconEnum.J, 200),
	A9(1, 8, ZctzIconEnum.T10, 250),
	A10(1, 9, ZctzIconEnum.T11, 300),
	A11(1, 10, ZctzIconEnum.SCATTER, 50),
	A12(1, 11, ZctzIconEnum.WILD, 50),
	A13(1, 12, ZctzIconEnum.A, 60),
	A14(1, 13, ZctzIconEnum.K, 80),
	A15(1, 14, ZctzIconEnum.Q, 100),
	A16(1, 15, ZctzIconEnum.J, 200),
	A17(1, 16, ZctzIconEnum.T10, 250),
	A18(1, 17, ZctzIconEnum.T11, 300),
	
	B1(2, 0, ZctzIconEnum.A, 1),
	B2(2, 1, ZctzIconEnum.K, 2),
	B3(2, 2, ZctzIconEnum.Q, 3),
	B4(2, 3, ZctzIconEnum.J, 4),
	B5(2, 4, ZctzIconEnum.T10, 60),
	B6(2, 5, ZctzIconEnum.T11, 80),
	B7(2, 6, ZctzIconEnum.A, 100),
	B8(2, 7, ZctzIconEnum.K, 200),
	B9(2, 8, ZctzIconEnum.Q, 250),
	B10(2, 9, ZctzIconEnum.J, 300),
	B11(2, 10, ZctzIconEnum.SCATTER, 50),
	B12(2, 11, ZctzIconEnum.WILD, 50),
	B13(2, 12, ZctzIconEnum.S1, 60),
	B14(2, 13, ZctzIconEnum.A1, 80),
	B15(2, 14, ZctzIconEnum.A2, 100),
	B16(2, 15, ZctzIconEnum.A3, 200),
	B17(2, 16, ZctzIconEnum.A1, 250),
	B18(2, 17, ZctzIconEnum.A2, 300),
	
	C1(3, 0, ZctzIconEnum.A, 1),
	C2(3, 1, ZctzIconEnum.K, 2),
	C3(3, 2, ZctzIconEnum.Q, 3),
	C4(3, 3, ZctzIconEnum.J, 4),
	C5(3, 4, ZctzIconEnum.T10, 60),
	C6(3, 5, ZctzIconEnum.T11, 80),
	C7(3, 6, ZctzIconEnum.A, 100),
	C8(3, 7, ZctzIconEnum.K, 200),
	C9(3, 8, ZctzIconEnum.Q, 250),
	C10(3, 9, ZctzIconEnum.J, 300),
	C11(3, 10, ZctzIconEnum.SCATTER, 50),
	C12(3, 11, ZctzIconEnum.WILD, 50),
	C13(3, 12, ZctzIconEnum.S1, 60),
	C14(3, 13, ZctzIconEnum.A1, 80),
	C15(3, 14, ZctzIconEnum.A2, 100),
	C16(3, 15, ZctzIconEnum.A3, 200),
	C17(3, 16, ZctzIconEnum.A1, 250),
	C18(33, 17, ZctzIconEnum.A2, 300),
	
	D1(4, 0, ZctzIconEnum.S1, 1),
	D2(4, 1, ZctzIconEnum.A1, 2),
	D3(4, 2, ZctzIconEnum.A2, 3),
	D4(4, 3, ZctzIconEnum.A3, 4),
	D5(4, 4, ZctzIconEnum.A, 60),
	D6(4, 5, ZctzIconEnum.K, 80),
	D7(4, 6, ZctzIconEnum.Q, 100),
	D8(4, 7, ZctzIconEnum.J, 200),
	D9(4, 8, ZctzIconEnum.T10, 250),
	D10(4, 9, ZctzIconEnum.T11, 300),
	D11(4, 10, ZctzIconEnum.SCATTER, 50),
	D12(4, 11, ZctzIconEnum.WILD, 50),
	D13(4, 12, ZctzIconEnum.A, 60),
	D14(4, 13, ZctzIconEnum.K, 80),
	D15(4, 14, ZctzIconEnum.Q, 100),
	D16(4, 15, ZctzIconEnum.J, 200),
	D17(4, 16, ZctzIconEnum.T10, 250),
	D18(4, 17, ZctzIconEnum.T11, 300),
	
	E1(5, 0, ZctzIconEnum.A, 1),
	E2(5, 1, ZctzIconEnum.K, 2),
	E3(5, 2, ZctzIconEnum.Q, 3),
	E4(5, 3, ZctzIconEnum.J, 4),
	E5(5, 4, ZctzIconEnum.T10, 60),
	E6(5, 5, ZctzIconEnum.T11, 80),
	E7(5, 6, ZctzIconEnum.A, 100),
	E8(5, 7, ZctzIconEnum.K, 200),
	E9(5, 8, ZctzIconEnum.Q, 250),
	E10(5, 9, ZctzIconEnum.J, 300),
	E11(5, 10, ZctzIconEnum.SCATTER, 50),
	E12(5, 11, ZctzIconEnum.WILD, 50),
	E13(5, 12, ZctzIconEnum.S1, 60),
	E14(5, 13, ZctzIconEnum.A1, 80),
	E15(5, 14, ZctzIconEnum.A2, 100),
	E16(5, 15, ZctzIconEnum.A3, 200),
	E17(5, 16, ZctzIconEnum.A1, 250),
	E18(5, 17, ZctzIconEnum.A2, 300),*/
	;
	
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private ZctzIconEnum icon;
	//权重
	private int weight;
	
	private ZctzRollerWeightEnum_backup(int id, int index, ZctzIconEnum icon, int weight){
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

	public ZctzIconEnum getIcon() {
		return icon;
	}

	public int getWeight() {
		return weight;
	}

	public static ZctzRollerWeightEnum_backup random(int c) {
		List<ZctzRollerWeightEnum_backup> all = new ArrayList<ZctzRollerWeightEnum_backup>();
		for(ZctzRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(ZctzRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		for(ZctzRollerWeightEnum_backup e : all){
			int w = v + e.getWeight();
			if(num >= v && num < w){
				return e;
			}else{
				v = w;
			}
		}
		return null;
	}

	/**
	 * 获取
	 * @param i
	 * @return
	 */
	public static Collection<? extends Window> windowInfo(int c) {
		
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<ZctzRollerWeightEnum_backup> all = new ArrayList<ZctzRollerWeightEnum_backup>();
		for(ZctzRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int random = RandomUtil.getRandom(0, all.size() - 1);
		for (int i = 1; i <= 3; i++) {
			ws.add(new WindowInfo(c, i, all.get(random).getIcon()));
			random ++ ;
			if (random > (all.size() - 1)) {
				random = 0;
			}
		}
		
		/*List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<ZctzRollerWeightEnum> all = new ArrayList<ZctzRollerWeightEnum>();
		for(ZctzRollerWeightEnum e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(ZctzRollerWeightEnum e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		ZctzRollerWeightEnum roller = null;
		Iterator<ZctzRollerWeightEnum> it = all.iterator();
		while(it.hasNext()){
			ZctzRollerWeightEnum e = it.next();
			int w = v + e.getWeight();
			if(num >= v && num < w){
				roller = e;
				break;
			}else{
				v = w;
			}
		}
		ws.add(new WindowInfo(c, 1, roller.getIcon()));
		int i=2;
		int index = 0;
		for(;i<=3;i++){
			if(it.hasNext()){
				roller = it.next();
			}else{
				roller = all.get(index);
				index++;
			}
			ws.add(new WindowInfo(c, i, roller.getIcon()));
		}*/
		return ws;
	}
}
