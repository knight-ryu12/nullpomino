/*
    Copyright (c) 2010, NullNoname
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:

        * Redistributions of source code must retain the above copyright
          notice, this list of conditions and the following disclaimer.
        * Redistributions in binary form must reproduce the above copyright
          notice, this list of conditions and the following disclaimer in the
          documentation and/or other materials provided with the distribution.
        * Neither the name of NullNoname nor the names of its
          contributors may be used to endorse or promote products derived from
          this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
    ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
    LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
    CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
    SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
    INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
    CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
    ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
    POSSIBILITY OF SUCH DAMAGE.
*/
package org.game_host.hebo.nullpomino.game.component;

import java.io.Serializable;

/**
 * 壁蹴り結果のクラス
 */
public class WallkickResult implements Serializable {
	/** シリアルバージョンID */
	private static final long serialVersionUID = -7985029240622355609L;

	/** X座標補正量 */
	public int offsetX;

	/** Y座標補正量 */
	public int offsetY;

	/** 回転後のピースの方向 */
	public int direction;

	/**
	 * コンストラクタ
	 */
	public WallkickResult() {
		reset();
	}

	/**
	 * パラメータ付きコンストラクタ
	 * @param offsetX X座標補正量
	 * @param offsetY Y座標補正量
	 * @param direction 回転後のテトラミノの方向
	 */
	public WallkickResult(int offsetX, int offsetY, int direction) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.direction = direction;
	}

	/**
	 * コピーコンストラクタ
	 * @param w コピー元
	 */
	public WallkickResult(WallkickResult w) {
		copy(w);
	}

	/**
	 * 初期値に戻す
	 */
	public void reset() {
		offsetX = 0;
		offsetY = 0;
		direction = 0;
	}

	/**
	 * 別のWallkickResultからコピー
	 * @param w コピー元
	 */
	public void copy(WallkickResult w) {
		this.offsetX = w.offsetX;
		this.offsetY = w.offsetY;
		this.direction = w.direction;
	}

	/**
	 * 上方向への壁蹴りかどうか判定
	 * @return 上方向への壁蹴りのとき（offsetY < 0のとき）にtrue
	 */
	public boolean isUpward() {
		return (offsetY < 0);
	}
}