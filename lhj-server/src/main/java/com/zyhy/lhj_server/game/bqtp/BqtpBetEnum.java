/**
 * 
 */
package com.zyhy.lhj_server.game.bqtp;

import com.zyhy.common_lhj.Bet;

/**
 * @author ASUS
 *
 */
public enum BqtpBetEnum implements Bet{
	//0.01（最小）、0.05、0.10、0.25、0.50、1.00、2.00、5.00、（最大）
	a(0,0),
	b(1,0),
	c(2,0),
	d(3,0),
	e(4,0),
	f(5,0),
	g(6,0),
	h(7,0),
	i(8,0),
	j(9,0),
	k(10,0),
	l(11,0),
	m(12,0),
	n(13,0),
	o(14,0),
	p(15,0),
	q(16,0),
	r(17,0),
	s(18,0),
	t(19,0),
	u(20,0),
	v(21,0),
	w(22,0),
	x(23,0),
	y(24,0),
	z(25,0),
	a1(26,0),
	b1(27,0),
	c1(28,0),
	d1(29,0),
	e1(30,0),
	;
	
	private int id;
	
	private double betcoin;
	
	BqtpBetEnum(int id, double betcoin){
		this.id = id;
		this.betcoin = betcoin;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public double getBetcoin() {
		return betcoin;
	}

	@Override
	public double getMin() {
		return 0;
	}

	@Override
	public double getMax() {
		return 0;
	}

	public static BqtpBetEnum getById(int id) {
		for(BqtpBetEnum v : values()){
			if(v.getId() == id){
				return v;
			}
		}
		return null;
	}

	public static BqtpBetEnum getByBetcoin(double jetton) {
		for(BqtpBetEnum v : values()){
			if(v.getBetcoin() == jetton){
				return v;
			}
		}
		return null;
	}

	public void setBetcoin(double betcoin) {
		this.betcoin = betcoin;
	}

}
