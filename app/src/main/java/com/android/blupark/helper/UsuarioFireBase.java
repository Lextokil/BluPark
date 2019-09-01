package com.android.blupark.helper;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.blupark.activity.DashBoardActivity;
import com.android.blupark.config.ConfiguracaoFireBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuarioFireBase {

   public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguracaoFireBase.getFireBaseAutenticacao();
        return usuario.getCurrentUser();
    }

    public static boolean atualizarNomeUsuario(String nome){
      try {
          FirebaseUser user = getUsuarioAtual();
          UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();
          user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Log.d("Perfil", "Erro ao atualizar nome de perfil");
                }
              }

          });
          return true;
      }catch (Exception e){
        e.printStackTrace();
        return false;
      }

    }

    public static void toDashBoardActivity(final Activity activity){
        Intent i = new Intent(activity, DashBoardActivity.class);
        activity.startActivity(i);
    }

    public static String getIDUsuarioAtual(){
        return getUsuarioAtual().getUid();
    }
}
