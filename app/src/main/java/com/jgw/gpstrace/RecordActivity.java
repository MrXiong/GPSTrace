package com.jgw.gpstrace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jgw.gpstrace.base.ToolBarActivity;
import com.jgw.gpstrace.bean.Code;
import com.jgw.gpstrace.bean.Message;
import com.jgw.gpstrace.bean.Node;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

import static com.jgw.gpstrace.bean.Message.VALUE_LIST;

public class RecordActivity extends ToolBarActivity {


    @BindView(R.id.rv_list)
    RecyclerViewFinal mRvList;
    @BindView(R.id.btn_to_scan)
    Button btnToScan;

    private List<Message> valueList = new ArrayList<>();
    private CommonAdapter mCommonAdapter;
    private Node node;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        node = (Node) getIntent().getSerializableExtra(Node.NODE);
        valueList = putMessages(node.getNodes());

        initView();
    }


    private List<Message> putMessages(String[] values) {
        List<Message> valueList = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            Message msg = new Message();
            msg.setKey(values[i]);
            msg.setValueHint("请输入" + values[i]);
            valueList.add(msg);
        }

        return valueList;
    }

    private void initView() {
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mCommonAdapter = new CommonAdapter<Message>(this, R.layout.listitem_text, valueList) {
            @Override
            protected void convert(ViewHolder holder, Message msg, final int position) {
                holder.setText(R.id.tv_text, msg.getKey());
                holder.setText(R.id.et_text_value, msg.getValue());
                EditText editText = holder.getView(R.id.et_text_value);

                if (msg.getKey().equals("定位")) {
                    holder.setText(R.id.et_text_value, node.getLatitude() + ";" + node.getLongitude());
                    valueList.get(position).setValue(node.getLatitude() + ";" + node.getLongitude());
                }
                if (msg.getKey().equals("时间")) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
                    Date date = new Date(System.currentTimeMillis());
                    holder.setText(R.id.et_text_value, simpleDateFormat.format(date));
                    valueList.get(position).setValue(simpleDateFormat.format(date));
                }


                SpannableString ss = new SpannableString(msg.getValueHint());
                AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15, true);
                ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                editText.setHint(new SpannedString(ss));


                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        valueList.get(position).setValue(editable.toString());
                    }
                });
            }
        };
        mRvList.setAdapter(mCommonAdapter);

    }

    @OnClick(R.id.btn_to_scan)
    public void onViewClicked() {
        Intent in = new Intent(this, ScanActivity.class);
        in.putExtra(VALUE_LIST, (Serializable) valueList);
        in.putExtra(Node.NODE, node);
        startActivity(in);
        finish();
    }

}
