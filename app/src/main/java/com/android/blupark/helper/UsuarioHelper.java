package com.android.blupark.helper;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.blupark.activity.AtivarTicketActivity;
import com.android.blupark.activity.CadastroVeiculoActivity;
import com.android.blupark.activity.CompraTicketsActivity;
import com.android.blupark.activity.DashBoardActivity;
import com.android.blupark.config.ConfiguracaoFireBase;
import com.android.blupark.model.Veiculo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsuarioHelper {

    public static boolean isTicketAtivo = false;
    public static Veiculo veiculo;



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

    public static void toCadastroVeiculoActivity(final Activity activity){
        Intent i = new Intent(activity, CadastroVeiculoActivity.class);
        activity.startActivity(i);
    }
    public static void toCompraTicketsActivity(final Activity activity){
        Intent i = new Intent(activity, CompraTicketsActivity.class);
        activity.startActivity(i);
    }
    public static void toAtivarTicketsActivity(final Activity activity){
        Intent i = new Intent(activity, AtivarTicketActivity.class);
        activity.startActivity(i);
    }

    public static String getIDUsuarioAtual(){
        return getUsuarioAtual().getUid();
    }

    public  static void addTicketUsuarioAtual(final int tickets){
        final DatabaseReference ticketsRef = FirebaseDatabase.getInstance().getReference("usuarios")
                .child(getIDUsuarioAtual()).child("qtdTickets");

        ticketsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total =  dataSnapshot.getValue(int.class);
                total += tickets;
                ticketsRef.setValue(total);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
