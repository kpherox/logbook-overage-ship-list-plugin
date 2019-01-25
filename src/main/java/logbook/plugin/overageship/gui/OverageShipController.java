package logbook.plugin.overageship.gui;

import java.util.Comparator;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.ShipMst;
import logbook.internal.gui.ShortageShipItem;
import logbook.internal.gui.Tools.Tables;
import logbook.internal.gui.Tools.Conrtols;
import logbook.internal.gui.WindowController;
import logbook.internal.LoggerHolder;
import logbook.internal.Ships;

/**
 * お風呂に入りたい艦娘のコントローラー
 *
 */
public class OverageShipController extends WindowController {

    @FXML
    private TableView<ShortageShipItem> table;

    /** ID */
    @FXML
    private TableColumn<ShortageShipItem, Integer> id;

    /** 艦娘 */
    @FXML
    private TableColumn<ShortageShipItem, Ship> ship;

    /** Lv */
    @FXML
    private TableColumn<ShortageShipItem, Integer> lv;

    /** 改装Lv */
    @FXML
    private TableColumn<ShortageShipItem, Integer> afterLv;

    private ObservableList<ShortageShipItem> item = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        Tables.setVisible(this.table, this.getClass().toString() + "#" + "table");
        Tables.setWidth(this.table, this.getClass().toString() + "#" + "table");
        Tables.setSortOrder(this.table, this.getClass().toString() + "#" + "table");

        // カラムとオブジェクトのバインド
        this.id.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.ship.setCellValueFactory(new PropertyValueFactory<>("ship"));
        this.ship.setCellFactory(p -> new ShipImageCell());
        this.lv.setCellValueFactory(new PropertyValueFactory<>("lv"));
        this.afterLv.setCellValueFactory(new PropertyValueFactory<>("afterLv"));

        // 改装レベル不足の艦娘
        this.table.setItems(this.item);
        this.overageShip();
    }

    /**
     * クリップボードにコピー
     */
    @FXML
    void copy() {
        Tables.selectionCopy(this.table);
    }

    /**
     * すべてを選択
     */
    @FXML
    void selectAll() {
        Tables.selectAll(this.table);
    }

    /**
     * テーブル列の表示・非表示の設定
     */
    @FXML
    void columnVisible() {
        try {
            Tables.showVisibleSetting(this.table, this.getClass().toString() + "#" + "table",
                    this.getWindow());
        } catch (Exception e) {
            LoggerHolder.get().error("FXMLの初期化に失敗しました", e);
        }
    }

    /**
     * 艦娘画像のセル
     *
     */
    private static class ShipImageCell extends TableCell<ShortageShipItem, Ship> {
        @Override
        protected void updateItem(Ship ship, boolean empty) {
            super.updateItem(ship, empty);

            if (!empty) {
                this.setGraphic(Conrtols.zoomImage(new ImageView(Ships.shipWithItemImage(ship))));
                this.setText(Ships.shipMst(ship)
                        .map(ShipMst::getName)
                        .orElse(""));
            } else {
                this.setGraphic(null);
                this.setText(null);
            }
        }
    }

    /**
     * 改装可能な艦娘の一覧を作る
     */
    private void overageShip() {
        this.item.addAll(ShipCollection.get()
                .getShipMap()
                .values()
                .stream()
                .map(ShortageShipItem::toShipItem)
                .filter(item -> item.getAfterLv() != 0 && item.getAfterLv() <= item.getLv())
                .sorted(Comparator.comparing(ShortageShipItem::getLv).reversed())
                .collect(Collectors.toList()));
    }
}
