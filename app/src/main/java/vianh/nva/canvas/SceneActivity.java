package vianh.nva.canvas;

import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SceneActivity extends AppCompatActivity {
    private Scene scene1, scene2, scene3;
    private Button btn1, btn2, btn3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scence);
        ViewGroup sceneRoot = findViewById(R.id.sceneRoot);
        btn1 = findViewById(R.id.btnChangeScene1);
        btn2 = findViewById(R.id.btnChangeScene2);
        btn3 = findViewById(R.id.btnChangeScene3);
        scene1 = Scene.getSceneForLayout(sceneRoot, R.layout.scene1, this);
        scene2 = Scene.getSceneForLayout(sceneRoot, R.layout.scene2, this);
        scene3 = Scene.getSceneForLayout(sceneRoot, R.layout.scene3, this);
        btn1.setOnClickListener(v -> {
            ChangeBounds changeBounds = new ChangeBounds();
            changeBounds.setInterpolator(new DecelerateInterpolator());
            TransitionManager.go(scene1, changeBounds);
        });
        btn2.setOnClickListener(v -> {
//            Transition transitionSet = TransitionInflater.from(this).inflateTransition(R.transition.slide_changebounds);
            ChangeBounds changeBounds = new ChangeBounds();
            changeBounds.setInterpolator(new DecelerateInterpolator());
            TransitionManager.go(scene2, changeBounds);
        });

        btn3.setOnClickListener(v -> {
            ChangeBounds changeBounds = new ChangeBounds();
            changeBounds.setInterpolator(new DecelerateInterpolator());
            TransitionManager.go(scene3, changeBounds);
        });
    }
}
