package Main;

/* Basic implementation of a skin */

import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;

public class CellControlSkin extends SkinBase<CellControl> implements
Skin<CellControl>{

	public CellControlSkin(CellControl control) {
		super(control);
	}
}
