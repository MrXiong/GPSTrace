package com.jgw.gpstrace;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jgw.gpstrace.bean.Code;
import com.jgw.gpstrace.bean.Message;
import com.jgw.gpstrace.bean.Node;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

import static com.jgw.gpstrace.bean.Message.VALUE_LIST;
import static org.litepal.crud.DataSupport.saveAll;

public class ScanActivity extends BaseScanActivity {

    @BindView(R.id.ll_content)
    LinearLayout mLlContent;
    @BindView(R.id.rv_list)
    RecyclerViewFinal mRvList;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.tv_saved)
    TextView tvSaved;
    private CommonAdapter mCommonAdapter;
    private List<String> mCodeList = new ArrayList<>();
    private List<Code> mCodeBeanList = new ArrayList<>();
    private List<Message> valueList = new ArrayList<>();
    private Node node;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);

        valueList = (List<Message>) getIntent().getSerializableExtra(VALUE_LIST);
        node = (Node) getIntent().getSerializableExtra(Node.NODE);

        setScanBackCallBack(new ScanBackCallBack() {
            @Override
            public void scanBackHandleDo(String scanResult) {
                if (checkList(mCodeList, scanResult)) {
                    Toast.makeText(ScanActivity.this, "码已存在", Toast.LENGTH_SHORT).show();
                    return;
                }
                mCodeList.add(scanResult);
                mCommonAdapter.notifyDataSetChanged();
            }
        });

        initView();
    }

    private void initView() {
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mCommonAdapter = new CommonAdapter<String>(this, R.layout.item_common_num, mCodeList) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tv_code, s);
            }
        };
        mRvList.setAdapter(mCommonAdapter);
    }


    private boolean checkList(List<String> codeList, String code) {
        if (!ListUtils.isEmpty(codeList)) {
            for (int i = 0; i < codeList.size(); i++) {
                if (codeList.get(i).equals(code)) {
                    return true;
                }
            }
        }

        return false;
    }

    @OnClick(R.id.btn_ok)
    public void onViewClicked() {
        if (ListUtils.isEmpty(mCodeList)) {
            Toast.makeText(ScanActivity.this, "请扫码...", Toast.LENGTH_SHORT).show();
            return;
        }
        //保存



        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mCodeList.size(); i++) {
                    Code code = new Code();
                    code.setMessageList(valueList);
                    code.setCode(mCodeList.get(i));
                    code.setNodeName(node.getNodeName());
                    code.setLatitude(node.getLatitude());
                    code.setLongitude(node.getLongitude());
                    code.save();
                    mCodeBeanList.add(code);
                }


                for (int j = 0; j < valueList.size(); j++) {
                    Message message = valueList.get(j);
                    message.setCodeList(mCodeBeanList);
                    message.save();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvSaved.setVisibility(View.VISIBLE);
                        mLlContent.setVisibility(View.GONE);
                        tvSaved.setText("正在保存...");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tvSaved.setText("保存成功");
                            }
                        }, 1500);

                    }
                });
            }
        }).start();


    }
}
