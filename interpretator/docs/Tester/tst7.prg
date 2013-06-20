begin
	! Ett mer komplext program som innehåller en komplexare rekursiv function;
	integer function F(X); integer X;
	begin
		if X>0 then F:=X+F(X-1) 
		else
		begin
			integer K;
			K:=I-1;
			F:=K; 
		end
	end;
	integer I;
	I:=3;
	begin
		integer K;
		K:=F(I+1)*2;
		I:=K+1;
	end;
	I:=I+F(2)*I+F(I);
end
