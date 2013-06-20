begin
   integer function F(X); real X;
   begin
       F:=R(N,X);
   end;

   integer function R(M,Y); integer M; real Y;
   begin
      integer function RR(Z); real Z;
      if F(Z)>5 then  RR:=Z-5  else RR:=M+Z;
      integer K;
      N:=N-1;
      if M>0 then R:=M*RR(Y)+R(M-1,Y-4)
      else R:=1;
   end;
   integer N;

   N:=2;
   write(R(3,readreal));
end

