package com.agora.classroom.agorapp.classroom.fragment;

import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioGroup;


import com.agora.classroom.agorabase.ToastManager;
import com.agora.classroom.agorapp.R;
import com.agora.classroom.agorapp.base.BaseFragment;
import com.agora.classroom.agorapp.classroom.widget.whiteboard.ApplianceView;
import com.agora.classroom.agorapp.classroom.widget.whiteboard.ColorPicker;
import com.agora.classroom.agorapp.classroom.widget.whiteboard.PageControlView;
import com.agora.classroom.agorapp.util.ColorUtil;

import butterknife.BindView;
import butterknife.OnTouch;

public class WhiteBoardFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, PageControlView.PageControlListener, BoardEventListener {

    @BindView(R.id.white_board_view)
    protected WhiteboardView white_board_view;
    @BindView(R.id.appliance_view)
    protected ApplianceView appliance_view;
    @BindView(R.id.color_select_view)
    protected ColorPicker color_select_view;
    @BindView(R.id.page_control_view)
    protected PageControlView page_control_view;
    @BindView(R.id.pb_loading)
    protected ProgressBar pb_loading;

    private WhiteSdk whiteSdk;
    private BoardManager boardManager = new BoardManager();

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_white_board;
    }

    @Override
    protected void initData() {
        WhiteSdkConfiguration configuration = new WhiteSdkConfiguration(DeviceType.touch, 10, 0.1);
        whiteSdk = new WhiteSdk(white_board_view, context, configuration);
        boardManager.setListener(this);
    }

    @Override
    protected void initView() {
        white_board_view.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            boardManager.refreshViewSize();
        });
        appliance_view.setVisibility(boardManager.isDisableDeviceInputs() ? View.GONE : View.VISIBLE);
        appliance_view.setOnCheckedChangeListener(this);
        color_select_view.setChangedListener(color -> {
            appliance_view.check(appliance_view.getApplianceId(boardManager.getAppliance()));
            boardManager.setStrokeColor(ColorUtil.colorToArray(color));
        });
        page_control_view.setListener(this);
    }

    public void initBoard(String uuid, String token) {
        if (TextUtils.isEmpty(uuid)) return;
        boardManager.getRoomPhase(new Promise<RoomPhase>() {
            @Override
            public void then(RoomPhase phase) {
                if (phase != RoomPhase.connected) {
                    pb_loading.setVisibility(View.VISIBLE);
                    boardManager.roomJoin(uuid, token, new Callback<RoomJoin>() {
                        @Override
                        public void onSuccess(RoomJoin res) {
                            RoomParams params = new RoomParams(uuid, res.roomToken);
                            boardManager.init(whiteSdk, params);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            ToastManager.showShort(throwable.getMessage());
                        }
                    });
                }
            }

            @Override
            public void catchEx(SDKError t) {
                ToastManager.showShort(t.getMessage());
            }
        });
    }

    public void disableDeviceInputs(boolean disabled) {
        if (appliance_view != null) {
            appliance_view.setVisibility(disabled ? View.GONE : View.VISIBLE);
        }
        boardManager.disableDeviceInputs(disabled);
    }

    public void disableCameraTransform(boolean disabled) {
        boardManager.disableCameraTransform(disabled);
    }

    public void releaseBoard() {
        boardManager.disconnect();
    }

    @OnTouch(R.id.white_board_view)
    boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            white_board_view.requestFocus();
        }
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        color_select_view.setVisibility(View.GONE);
        switch (checkedId) {
            case R.id.tool_selector:
                boardManager.setAppliance(Appliance.SELECTOR);
                break;
            case R.id.tool_pencil:
                boardManager.setAppliance(Appliance.PENCIL);
                break;
            case R.id.tool_text:
                boardManager.setAppliance(Appliance.TEXT);
                break;
            case R.id.tool_eraser:
                boardManager.setAppliance(Appliance.ERASER);
                break;
            case R.id.tool_color:
                color_select_view.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void toStart() {
        boardManager.setSceneIndex(0);
    }

    @Override
    public void toPrevious() {
        boardManager.pptPreviousStep();
    }

    @Override
    public void toNext() {
        boardManager.pptNextStep();
    }

    @Override
    public void toEnd() {
        boardManager.setSceneIndex(boardManager.getSceneCount() - 1);
    }

    @Override
    public void onRoomPhaseChanged(RoomPhase phase) {
        pb_loading.setVisibility(phase == RoomPhase.connected ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onSceneStateChanged(SceneState state) {
        page_control_view.setPageIndex(state.getIndex(), state.getScenes().length);
    }

    @Override
    public void onMemberStateChanged(MemberState state) {
        appliance_view.check(appliance_view.getApplianceId(state.getCurrentApplianceName()));
    }

}
