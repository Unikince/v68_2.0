package com.dmg.bairenzhajinhuaserver.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;
import com.dmg.bairenzhajinhuaserver.model.constants.Combination;

/**
 * @description: 牌
 * @return
 * @author mice
 * @date 2019/7/29
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Poker implements Comparable<Poker>, Serializable {
	private static final long serialVersionUID = 1L;
	private int value; // 牌值
    private int type; // 花色 1:黑 2:红 3:樱 4:方  //1 方块 2 梅花 3红桃 4黑桃

    @Override
    public Poker clone() {
        return new Poker(value,type);
    }

    @Override
    public int compareTo(Poker o) {
    	if(this.value == 1) {
    		this.value = this.value + 13;
    	}
    	if(o.value == 1) {
    		o.value = o.value + 13;
    	}
        if (this.value < o.value) {
        	if(o.value == 14) {
        		o.value = 1;
        	}
            return 1;
        }
        if (this.value > o.value) {
        	if(this.value == 14) {
        		this.value = 1;
        	}
            return -1;
        }
        if(this.value == 14) {
    		this.value = 1;
    	}
        if(o.value == 14) {
    		o.value = 1;
    	}
        return 0;
    }
    
    /**
     * 获取牌型
     * @param pokerList
     * @return
     */
    public static int getCartType(List<Poker> pokerList) {
    	int cardType = 0;
        int[] pokers = new int[3];
        int[] types = new int[3];
        for (int i = 0; i < pokerList.size(); i++) {
            pokers[i] = pokerList.get(i).getValue();
            types[i] = pokerList.get(i).getType();
        }
        int colorCount = getSameCardOrColor(types);
        boolean shunZi = shunZi(pokers);
        switch (getSameCardOrColor(pokers)) {
		case 3://豹子
			cardType = Combination.LEOPARD.getValue();
			break;
		case 2://对子
			cardType = Combination.PAIR.getValue();
			break;
		case 1:
			if(colorCount == 3) {//顺金、金花
				if(shunZi) {
					cardType = Combination.STRAIGHTFLUSH.getValue();
				} else {
					cardType = Combination.FLUSH.getValue();
					if (pokers[0] == 1 && pokers[1] == 12 && pokers[2] == 13) {
                    	cardType = Combination.STRAIGHTFLUSH.getValue();
                    } 
				}
			} else {//顺子、散牌
				if(shunZi) {
					cardType = Combination.PROGRESSION.getValue();
				} else {
					cardType = Combination.HIGHCARD.getValue();
					//特殊牌型
//					if (pokers[0] + pokers[1] + pokers[2] == 10 && pokers[0] == 2) {
//	                	cardType = Combination.SPECIAL.getValue();
//					} 
					if (pokers[0] == 1 && pokers[1] == 12 && pokers[2] == 13) {
                    	cardType = Combination.PROGRESSION.getValue();
                    }
				}
			}
			break;
		default:
			break;
		}
        return cardType;
    }
    
    /**
     * 获取相同牌值/花色数量
     * @param pokers
     * @return
     */
    private static int getSameCardOrColor(int[] pokers) {
    	int samcount = 1;
    	int samValue = 0;
    	for(int value : pokers) {
    		if(samValue != value) {
    			samValue = value;
    		} else {
    			samcount ++;
    		}
    	}
		return samcount;
    }
    
    /**
     * 判断是否顺子
     * @param pokers
     * @return
     */
    private static boolean shunZi(int[] pokers) {
    	if(pokers[0] + pokers[2] == pokers[1] * 2 && pokers[0] + 1 == pokers[1]) {
    		return true;
    	}
    	return false;
    }
    
}
