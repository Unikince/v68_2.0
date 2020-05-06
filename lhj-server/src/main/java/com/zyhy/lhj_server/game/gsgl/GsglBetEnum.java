/**
 * 
 */
package com.zyhy.lhj_server.game.gsgl;

import com.zyhy.common_lhj.Bet;

/**
 * @author ASUS
 *
 */
public enum GsglBetEnum implements Bet{
	//0.01、0.02、0.05、0.08、0.16、0.80、1.60、2、4、6、8、16、40、80、160、200、400、600、800
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
	
	GsglBetEnum(int id, double betcoin){
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

	public static GsglBetEnum getById(int id) {
		for(GsglBetEnum v : values()){
			if(v.getId() == id){
				return v;
			}
		}
		return null;
	}

	public static GsglBetEnum getByBetcoin(double jetton) {
		for(GsglBetEnum v : values()){
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
