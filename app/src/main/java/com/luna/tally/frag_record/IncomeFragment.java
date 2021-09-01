package com.luna.tally.frag_record;


import androidx.fragment.app.Fragment;

import com.luna.tally.R;
import com.luna.tally.db.DBManager;
import com.luna.tally.db.TypeBean;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeFragment extends BaseRecordFragment {

    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        // 获取数据库中数据源
        List<TypeBean> outlist = DBManager.getTypeList(1);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.in_qt_fs);
    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(1);
        DBManager.insertItemToAccounttb(accountBean);
    }
}