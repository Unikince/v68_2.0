/**
 * 
 */
package com.zyhy.lhj_server.game.lqjx;

import com.zyhy.common_lhj.Bet;

/**
 * @author ASUS
 *
 */
public enum LqjxBetEnum implements Bet{
	//0.01、0.02、0.03、0.04、0.05、0.06、0.07、0.08、0.09、0.10、0.12、0.14、0.15、0.16、0.18、0.20、0.25、0.30、0.35、0.40、0.45、0.50、0.60、0.70、0.80、0.90、1。
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
	
	LqjxBetEnum(int id, double betcoin){
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

	public static LqjxBetEnum getById(int id) {
		for(LqjxBetEnum v : values()){
			if(v.getId() == id){
				return v;
			}
		}
		return null;
	}

	public static LqjxBetEnum getByBetcoin(double jetton) {
		for(LqjxBetEnum v : values()){
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
