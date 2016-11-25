package Client_SSL;
/*
Joseph Salomone
660658959
salomj
Crypto Project
*/
import java.io.*;
import java.net.*;
import java.util.*;

abstract class Methods
{
	public abstract void SetKey(String NewKey);
	public abstract String Encrypt(String Text);
	public abstract String Decrypt(String Text);
}
class RC4 extends Methods
{
	private int[] Key;
	public void SetKey(String NewKey)
	{
		Key = new int[(NewKey.length() + 2) / 3];
		int x = 0, y = 0;
		for(int i = 0; i < NewKey.length(); i++)
		{
			switch(NewKey.charAt(i))
			{
				case ' ':
					Key[x] = y;
					y = 0;
					x++;
					break;
				case '0':
					y = y * 16;
					break;
				case '1':
					y = y * 16 + 1;
					break;
				case '2':
					y = y * 16 + 2;
					break;
				case '3':
					y = y * 16 + 3;
					break;
				case '4':
					y = y * 16 + 4;
					break;
				case '5':
					y = y * 16 + 5;
					break;
				case '6':
					y = y * 16 + 6;
					break;
				case '7':
					y = y * 16 + 7;
					break;
				case '8':
					y = y * 16 + 8;
					break;
				case '9':
					y = y * 16 + 9;
					break;
				case 'a':
				case 'A':
					y = y * 16 + 10;
					break;
				case 'b':
				case 'B':
					y = y * 16 + 11;
					break;
				case 'c':
				case 'C':
					y = y * 16 + 12;
					break;
				case 'd':
				case 'D':
					y = y * 16 + 13;
					break;
				case 'e':
				case 'E':
					y = y * 16 + 14;
					break;
				case 'f':
				case 'F':
					y = y * 16 + 15;
					break;
			}
		}
		Key[x] = y;
	}
	private String intbin(int y)
	{
		String s = new String();
		while(y > 0)
		{
			switch(y%2)
			{
				case 1:
					s = '1' + s;
					break;
				case 0:
					s = '0' + s;
					break;
			}
			y /= 2;
		}
		while (s.length() < 8)
		{
			s = '0' + s;
		}
		return s;
	}
	private char XOR(char c1, char c2)
	{
		if((c1 == '0' && c2 == '0') || (c1 == '1' && c2 == '1'))
		{
			return '0';
		}
		else
		{
			return '1';
		}
	}
	private String XOR(String s1, String s2)
	{
		return "" +
				XOR(s1.charAt(1-1), s2.charAt(1-1)) +
				XOR(s1.charAt(2-1), s2.charAt(2-1)) +
				XOR(s1.charAt(3-1), s2.charAt(3-1)) +
				XOR(s1.charAt(4-1), s2.charAt(4-1)) +
				XOR(s1.charAt(5-1), s2.charAt(5-1)) +
				XOR(s1.charAt(6-1), s2.charAt(6-1)) +
				XOR(s1.charAt(7-1), s2.charAt(7-1)) +
				XOR(s1.charAt(8-1), s2.charAt(8-1));
	}
	public String Encrypt(String Text)
	{
		String cipher = new String();
		int[] key = new int[256];
		int[] sbox = new int[256];
		for(int i = 0; i < 256; i++)
		{
			key[i] = Key[i % Key.length];
			sbox[i] = i;
		}
		int y = 0;
		for(int i = 0; i < 256; i++)
		{
			y = (y + sbox[i] + key[i]) % 256;
			int temp = sbox[i];
			sbox[i] = sbox[y];
			sbox[y] = temp;
		}
		
		y = 0;
		int x = 0, g = 0;
		for(int i = 0; i < Text.length(); i += 8)
		{
			x = (x + 1) % 256;
			y = (y + sbox[x]) % 256;
			int temp = sbox[x];
			sbox[x] = sbox[y];
			sbox[y] = temp;
			g = sbox[(sbox[x] + sbox[y]) % 256];
			cipher = cipher + XOR(Text.substring(i, i+8), intbin(g));
		}
		return cipher;
	}
	public String Decrypt(String Text)
	{
		return Encrypt(Text);
	}
}
class SDES extends Methods
{
	private String Key;
	public void SetKey(String NewKey)
	{
		Key = new String();
		for(int i = 0; Key.length() < 10; i++)
		{
			switch(NewKey.charAt(i))
			{
				case '0':
					Key = Key + "0000";
					break;
				case '1':
					Key = Key + "0001";
					break;
				case '2':
					Key = Key + "0010";
					break;
				case '3':
					Key = Key + "0011";
					break;
				case '4':
					Key = Key + "0100";
					break;
				case '5':
					Key = Key + "0101";
					break;
				case '6':
					Key = Key + "0110";
					break;
				case '7':
					Key = Key + "0111";
					break;
				case '8':
					Key = Key + "1000";
					break;
				case '9':
					Key = Key + "1001";
					break;
				case 'a':
				case 'A':
					Key = Key + "1010";
					break;
				case 'b':
				case 'B':
					Key = Key + "1011";
					break;
				case 'c':
				case 'C':
					Key = Key + "1100";
					break;
				case 'd':
				case 'D':
					Key = Key + "1101";
					break;
				case 'e':
				case 'E':
					Key = Key + "1110";
					break;
				case 'f':
				case 'F':
					Key = Key + "1111";
					break;
			}
		}
		Key = Key.substring(0, 10);
	}
	private String KeyIntial(String Key)
	{
		return "" + 
				Key.charAt(3-1) + 
				Key.charAt(5-1) + 
				Key.charAt(2-1) + 
				Key.charAt(7-1) + 
				Key.charAt(4-1) + 
				Key.charAt(10-1) + 
				Key.charAt(1-1) + 
				Key.charAt(9-1) + 
				Key.charAt(8-1) + 
				Key.charAt(6-1);
	}
	private String KeyShift(String Key)
	{
		return "" + 
				Key.charAt(2-1) + 
				Key.charAt(3-1) + 
				Key.charAt(4-1) + 
				Key.charAt(5-1) + 
				Key.charAt(1-1) + 
				Key.charAt(7-1) + 
				Key.charAt(8-1) + 
				Key.charAt(9-1) + 
				Key.charAt(10-1) + 
				Key.charAt(6-1);
	}
	private String KeyFinal(String Key)
	{
		return "" + 
				Key.charAt(6-1) + 
				Key.charAt(3-1) + 
				Key.charAt(7-1) + 
				Key.charAt(4-1) + 
				Key.charAt(8-1) + 
				Key.charAt(5-1) + 
				Key.charAt(10-1) + 
				Key.charAt(9-1);
	}
	private String TextIntial(String Text)
	{
		return "" + 
				Text.charAt(2-1) + 
				Text.charAt(6-1) + 
				Text.charAt(3-1) + 
				Text.charAt(1-1) + 
				Text.charAt(4-1) + 
				Text.charAt(8-1) + 
				Text.charAt(5-1) + 
				Text.charAt(7-1);
	}
	private char XOR(char Text, char Key)
	{
		if((Text == '0' && Key == '0') || (Text == '1' && Key == '1'))
		{
			return '0';
		}
		else
		{
			return '1';
		}
	}
	private String XOR(String Text, String Key)
	{
		return "" + 
				XOR(Text.charAt(1-1), Key.charAt(1-1)) + 
				XOR(Text.charAt(2-1), Key.charAt(2-1)) + 
				XOR(Text.charAt(3-1), Key.charAt(3-1)) + 
				XOR(Text.charAt(4-1), Key.charAt(4-1)) + 
				XOR(Text.charAt(5-1), Key.charAt(5-1)) + 
				XOR(Text.charAt(6-1), Key.charAt(6-1)) + 
				XOR(Text.charAt(7-1), Key.charAt(7-1)) + 
				XOR(Text.charAt(8-1), Key.charAt(8-1));
	}
	private String XOR2(String Text, String Key)
	{
		return "" + 
				XOR(Text.charAt(1-1), Key.charAt(1-1)) + 
				XOR(Text.charAt(2-1), Key.charAt(2-1)) + 
				XOR(Text.charAt(3-1), Key.charAt(3-1)) + 
				XOR(Text.charAt(4-1), Key.charAt(4-1));
	}
	private String TextFinal(String Text)
	{
		return "" + 
				Text.charAt(4-1) + 
				Text.charAt(1-1) + 
				Text.charAt(3-1) + 
				Text.charAt(5-1) + 
				Text.charAt(7-1) + 
				Text.charAt(2-1) + 
				Text.charAt(8-1) + 
				Text.charAt(6-1);
	}
	private String Expansion(String Text)
	{
		return "" +
			Text.charAt(8-1) +
			Text.charAt(5-1) +
			Text.charAt(6-1) +
			Text.charAt(7-1) +
			Text.charAt(6-1) +
			Text.charAt(7-1) +
			Text.charAt(8-1) +
			Text.charAt(5-1);
	}
	private int GetInt(char c1, char c2)
	{
		if(c1 == '1')
		{
			if(c2 == '1')
			{
				return 3;
			}
			else
			{
				return 2;
			}
		}
		else
		{
			if(c2 == '1')
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
	}
	private String S0(String Text)
	{
		String[][] SBox = {{"01", "00", "11", "10"}, {"11", "10", "01", "00"}, {"00", "10", "01", "11"}, {"11", "01", "11", "10"}};
		return SBox[GetInt(Text.charAt(1-1), Text.charAt(4-1))][GetInt(Text.charAt(2-1), Text.charAt(3-1))];
	}
	private String S1(String Text)
	{
		String[][] SBox = {{"00", "01", "10", "11"}, {"10", "00", "01", "11"}, {"11", "00", "01", "00"}, {"10", "01", "00", "11"}};
		return SBox[GetInt(Text.charAt(5-1), Text.charAt(8-1))][GetInt(Text.charAt(6-1), Text.charAt(7-1))];
	}
	private String FFinal(String Text)
	{
		return "" + 
				Text.charAt(2-1) + 
				Text.charAt(4-1) + 
				Text.charAt(3-1) + 
				Text.charAt(1-1);
	}
	private String FBox(String Text, String Key)
	{
		String SubText = XOR(Expansion(Text), Key);
		return FFinal(S0(SubText) + S1(SubText));
	}
	private String DoEncrypt(String Text)
	{
		String K1 = KeyFinal(KeyShift(KeyIntial(Key)));
		String K2 = KeyFinal(KeyShift(KeyShift(KeyShift(KeyIntial(Key)))));
		Text = TextIntial(Text);
		String Output = FBox(Text, K1);
		Output = XOR2(Output, Text);
		Text = Text.substring(4) + Output;
		Output = FBox(Text, K2);
		Output = XOR2(Output, Text);
		Text = Output + Text.substring(4);
		return TextFinal(Text);	
	}
	private String DoDecrypt(String Text)
	{
		String K1 = KeyFinal(KeyShift(KeyIntial(Key)));
		String K2 = KeyFinal(KeyShift(KeyShift(KeyShift(KeyIntial(Key)))));
		Text = TextIntial(Text);
		String Output = FBox(Text, K2);
		Output = XOR2(Output, Text);
		Text = Text.substring(4) + Output;
		Output = FBox(Text, K1);
		Output = XOR2(Output, Text);
		Text = Output + Text.substring(4);
		return TextFinal(Text);
	}
	public String Encrypt(String Text)
	{
		String s = new String();
		for(int i = 0; i < Text.length(); i += 8)
		{
			s = s + DoEncrypt(Text.substring(i, i+8));
		}
		return s;
	}
	public String Decrypt(String Text)
	{
		String s = new String();
		for(int i = 0; i < Text.length(); i += 8)
		{
			s = s + DoDecrypt(Text.substring(i, i+8));
		}
		return s;
	}
}

class Client_Protocol
{
	private String Challenge_Data;
	private Random R;
	private String Current_Message;
	private String MAC_Secret;
	private String Write_Key;
	private String Read_Key;
	private int Sequence_Number;
	private int Base;
	private int Mod;
	private int Server_Public;
	private int Client_Private;
	private int Client_Public;
	private int Master_Key;
	private String Connection_ID;
	private Methods Write;
	private Methods Read;
	private int Change_Cipher_Spec;
	static private String BinHex(String Hex)
	{
		String s = new String();
		int y = 0;
		for(int i = 0; i < Hex.length(); i += 4)
		{
			String Comp = Hex.substring(i, i+4);
			if(y > 1)
			{
				s = s + ' ';
				y = 0;
			}
			if(Comp.compareTo("0000") == 0)
			{
				s = s + '0';
			}
			else if(Comp.compareTo("0001") == 0)
			{
				s = s + '1';
			}
			else if(Comp.compareTo("0010") == 0)
			{
				s = s + '2';
			}
			else if(Comp.compareTo("0011") == 0)
			{
				s = s + '3';
			}
			else if(Comp.compareTo("0100") == 0)
			{
				s = s + '4';
			}
			else if(Comp.compareTo("0101") == 0)
			{
				s = s + '5';
			}
			else if(Comp.compareTo("0110") == 0)
			{
				s = s + '6';
			}
			else if(Comp.compareTo("0111") == 0)
			{
				s = s + '7';
			}
			else if(Comp.compareTo("1000") == 0)
			{
				s = s + '8';
			}
			else if(Comp.compareTo("1001") == 0)
			{
				s = s + '9';
			}
			else if(Comp.compareTo("1010") == 0)
			{
				s = s + 'A';
			}
			else if(Comp.compareTo("1011") == 0)
			{
				s = s + 'B';
			}
			else if(Comp.compareTo("1100") == 0)
			{
				s = s + 'C';
			}
			else if(Comp.compareTo("1101") == 0)
			{
				s = s + 'D';
			}
			else if(Comp.compareTo("1110") == 0)
			{
				s = s + 'E';
			}
			else if(Comp.compareTo("1111") == 0)
			{
				s = s + 'F';
			}
			y++;
		}
		return s;
	}
	static private String HexBin(String i)
	{
		String y = new String();
		for(int z = 0; z < i.length(); z++)
		{
			switch(i.charAt(z))
			{
				case '0':
					y = y + "0000";
					break;
				case '1':
					y = y + "0001";
					break;
				case '2':
					y = y + "0010";
					break;
				case '3':
					y = y + "0011";
					break;
				case '4':
					y = y + "0100";
					break;
				case '5':
					y = y + "0101";
					break;
				case '6':
					y = y + "0110";
					break;
				case '7':
					y = y + "0111";
					break;
				case '8':
					y = y + "1000";
					break;
				case '9':
					y = y + "1001";
					break;
				case 'a':
				case 'A':
					y = y + "1010";
					break;
				case 'b':
				case 'B':
					y = y + "1011";
					break;
				case 'c':
				case 'C':
					y = y + "1100";
					break;
				case 'd':
				case 'D':
					y = y + "1101";
					break;
				case 'e':
				case 'E':
					y = y + "1110";
					break;
				case 'f':
				case 'F':
					y = y + "1111";
					break;
			}
		}
		return y;
	}
	static private String HexInt(int i)
	{
		String s = new String();
		int y = i;
		int x = 0;
		while(y > 0)
		{
			if(x > 1)
			{
				s = " " + s;
				x = 0;
			}
			switch(y%16)
			{
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
					s = Integer.toString(y%16) + s;
					break;
				case 10:
					s = 'a' + s;
					break;
				case 11:
					s = 'b' + s;
					break;
				case 12:
					s = 'c' + s;
					break;
				case 13:
					s = 'd' + s;
					break;
				case 14:
					s = 'e' + s;
					break;
				case 15:
					s = 'f' + s;
					break;
			}
			y /= 16;
			x++;
		}
		if(s.length() == 0)
		{
			s = "00 00 00 00";
		}
		while(s.length() < 11)
		{
			if(x > 1)
			{
				s = "0 " + s;
				x = 1;
			}
			else
			{
				s = '0' + s;
				x++;
			}
		}
		return s;
	}
	static private String HexDec(int i)
	{
		String s = new String();
		int y = i;
		while(y > 0)
		{
			switch(y%16)
			{
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
					s = Integer.toString(y%16) + s;
					break;
				case 10:
					s = 'a' + s;
					break;
				case 11:
					s = 'b' + s;
					break;
				case 12:
					s = 'c' + s;
					break;
				case 13:
					s = 'd' + s;
					break;
				case 14:
					s = 'e' + s;
					break;
				case 15:
					s = 'f' + s;
					break;
			}
			y /= 16;
		}
		if(s.length() == 0)
		{
			s = "00";
		}
		else if(s.length() % 2 == 1)
		{
			s = '0' + s;
		}
		return s;
	}
	static private int HexDec(String i)
	{
		int y = 0;
		for(int z = 0; z < i.length(); z++)
		{
			if(i.codePointAt(z) != ' ')
			{
				y *= 16;
				switch(i.codePointAt(z))
				{
					case '0':
						break;
					case '1':
						y += 1;
						break;
					case '2':
						y += 2;
						break;
					case '3':
						y += 3;
						break;
					case '4':
						y += 4;
						break;
					case '5':
						y += 5;
						break;
					case '6':
						y += 6;
						break;
					case '7':
						y += 7;
						break;
					case '8':
						y += 8;
						break;
					case '9':
						y += 9;
						break;
					case 'a':
					case 'A':
						y += 10;
						break;
					case 'b':
					case 'B':
						y += 11;
						break;
					case 'c':
					case 'C':
						y += 12;
						break;
					case 'd':
					case 'D':
						y += 13;
						break;
					case 'e':
					case 'E':
						y += 14;
						break;
					case 'f':
					case 'F':
						y += 15;
						break;
				}
			}
		}
		return y;
	}
	private String Client_Hello()
	{
		String Message;
		int Length = (R.nextInt(64 - 16) + 16);
		Message = "00 " + HexDec(29 + Length) + " 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 08 " + HexDec(Length) + " 53 44 45 53 20 52 43 34 ";//Data
		Challenge_Data = HexDec(R.nextInt(255));
		for(int i = 1; i < Length; i++)//Add Challenge_Data
		{
			Challenge_Data += ' ' + HexDec(R.nextInt(255));
		}
		Message += Challenge_Data;
		while(Message.length() < 1535)//Add Padding
		{
			Message += ' ' + HexDec(R.nextInt(255));
		}
		return Message;
	}
	private int Server_Hello()
	{
		if(Current_Message.substring(0, 2).compareTo("02") == 0)
		{
			Base = HexDec(Current_Message.substring(3, 15));
			Mod = HexDec(Current_Message.substring(15, 27));
			Server_Public = HexDec(Current_Message.substring(27, 39));
			int specs = HexDec(Current_Message.substring(39, 42));
			int id = HexDec(Current_Message.substring(42, 45));
			Connection_ID = Current_Message.substring(45 + 3 * specs, 45 + 3 * specs + 3 * id - 1);
			if(Current_Message.substring(45, 45 + 3 * specs).compareTo("52 43 34 ") == 0)
			{
				Write = new RC4();
				Read = new RC4();
			}
			else if(Current_Message.substring(45, 45 + 3 * specs).compareTo("53 44 45 53 ") == 0)
			{
				Write = new SDES();
				Read = new SDES();
			}
			else
			{
				return -1;
			}
			return 0;
		}
		else
		{
			return 1;
		}
	}
	private String Client_Key_Exchange()
	{
		String Message = "00 " + HexDec(23 + (Connection_ID.length() * 2)/ 3) + " 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 04 " + HexInt(Client_Public) + " " + BinHex(Write.Encrypt(HexBin(Connection_ID)));
		while(Message.length() < 1535)//Add Padding
		{
			Message += ' ' + HexDec(R.nextInt(255));
		}
		return Message;
	}
	private int Server_Finish()
	{
		if(Current_Message.substring(0, 2).compareTo("08") == 0)
		{
			if(BinHex(Read.Decrypt(HexBin(Current_Message.substring(3, 3 + Challenge_Data.length())))).compareToIgnoreCase(Challenge_Data) != 0)
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}
		else
		{
			return 1;
		}
	}
	static private String MAC(String MAC_Data)
	{
		String s = HexBin(MAC_Data);
		char[] c = new char[128];
		for(int i = 0; i < 128; i++)
		{
			c[i] = '0';
		}
		for(int i = 0; i < s.length(); i++)
		{
			if((c[i%128] == '0' && s.charAt(i) == '0') || (c[i%128] == '1' && s.charAt(i) == '1'))
			{
				c[i%128] = '0';
			}
			else
			{
				c[i%128] = '1';
			}
		}
		return BinHex(new String(c));
	}
	private int GetMessage(String Message)
	{
		if(Change_Cipher_Spec == 0)
		{
			int Length = HexDec(Message.substring(0, 5));
			//String MAC = Message.substring(6, 53);
			Current_Message = Message.substring(54, Length * 3 - 1);
			//String Padding = Message.substring(Length * 3, 1535);
			return 1;
		}
		else
		{
			int Length = HexDec(Message.substring(0, 5));
			String s = BinHex(Read.Decrypt(HexBin(Message.substring(6))));
			String MAC = s.substring(0, 47);
			Current_Message = s.substring(48, Length * 3 - 7);
			String Padding = new String();
			if(Length < 512)
			{
				Padding = s.substring(Length * 3 - 6, 1529);
			}
			if(MAC(MAC_Secret + " " + Current_Message + " " + Padding + " " + HexInt(Sequence_Number)).compareToIgnoreCase(MAC) != 0)
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}
	}
	static private int PowMod(int base, int power, int mod)
	{
		if(mod < 1 || power < 0 || base < 0)
		{
			return -1;
		}
		if(power == 0)
		{
			return 1;
		}
		long y = (long)base % (long)mod;
		for(int i = 1; i < power; i++)
		{
			y = (y * (long)base) % (long)mod;
		}
		return (int)y;
	}
	//Client To Send Information
	public void ClientSend(String Server, int Port)
	{
		R = new Random();
		Change_Cipher_Spec = 0;
		try
		{
			//Connect to server
			Socket socket = new Socket(Server, Port);
			BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
			
			System.out.println("Started Client");
			System.out.println("Server is " + Server + ":" + Port);
			
			//--------------------
			//Client Hello
			//--------------------
			System.out.println("Client Hello");
			
			String Message = Client_Hello();
			toServer.print(Message);
			toServer.flush();
			
			System.out.println("Challenge Data: " + Challenge_Data);
			
			System.out.println();
			
			//--------------------
			//Server Hello
			//--------------------
			System.out.println("Server Hello");
			
			char[] GetValue = new char[1535];
			fromServer.read(GetValue, 0, 1535);
			
			if(GetValue[0] == 0)
			{
				System.out.println("Server Disconnected");
				return;
			}
			if(GetValue[1534] == 0)
			{
				System.out.println("Server Send Not Large Enough");
				return;
			}
			
			GetMessage(new String(GetValue));
			switch (Server_Hello())
			{
				case -1:
					System.err.println("No Encryption Model Used");
					return;
				case 0:
					if(Write instanceof SDES)
					{
						System.out.println("Using SDES");
					}
					else if(Write instanceof RC4)
					{
						System.out.println("Using RC4");
					}
					break;
				case 1:
					System.err.println("Unexpected Message Code: Expected 02");
					return;
			}
			
			System.out.println("Base: " + Base);
			System.out.println("Mod: " + Mod);
			System.out.println("Server Public Key: " + Server_Public);
			System.out.println("Connection ID: " + Connection_ID);
			
			System.out.println();
			
			//--------------------
			//Find Keys
			//--------------------
			System.out.println("Generating DH Keys");
			
			Client_Private = R.nextInt(Mod);
			Client_Public = PowMod(Base, Client_Private, Mod);
			Master_Key = PowMod(Server_Public, Client_Private, Mod);
			
			System.out.println("Client Private Key: " + Client_Private);
			System.out.println("Client Public Key: " + Client_Public);
			System.out.println("Shared Secret Key: " + Master_Key);
			
			System.out.println();
			
			//--------------------
			//Generate Final Keys
			//--------------------
			System.out.println("Generating Message Keys");
			Read_Key = MAC(HexInt(Master_Key) + " 00 " + Challenge_Data + " " + Connection_ID);
			Write_Key = MAC(HexInt(Master_Key) + " 01 " + Challenge_Data + " " + Connection_ID);
			MAC_Secret = MAC(HexInt(Master_Key) + " 02 " + Challenge_Data + " " + Connection_ID);
			
			Write.SetKey(Write_Key);
			Read.SetKey(Read_Key);
			
			System.out.println("Client Read Key: " + Read_Key);
			System.out.println("Client Write Key: " + Write_Key);
			System.out.println("MAC Secret Key: " + MAC_Secret);
			
			System.out.println();
			
			//--------------------
			//Client Key Exchange
			//--------------------
			System.out.println("Client Key Exchange");
			
			Message = Client_Key_Exchange();
			toServer.print(Message);
			toServer.flush();
			
			System.out.println();
			
			//--------------------
			//Server Finish
			//--------------------
			System.out.println("Server Finish");
			
			GetValue = new char[1535];
			fromServer.read(GetValue, 0, 1535);
			
			if(GetValue[0] == 0)
			{
				System.out.println("Server Disconnected");
				return;
			}
			if(GetValue[1534] == 0)
			{
				System.out.println("Server Send Not Large Enough");
				return;
			}
			
			GetMessage(new String(GetValue));
			switch(Server_Finish())
			{
				case -1:
					System.err.println("Challenge Data Mismatch");
					return;
				case 0:
					break;
				case 1:
					System.err.println("Unexpected Message Code: Expected 08");
					return;
			}
			
			Change_Cipher_Spec = 1;
			
			//--------------------
			//Data Transfer
			//--------------------
			System.out.println("Start Data Transfer");
			
			String s = new String();
			while(true)
			{
				GetValue = new char[1535];
				fromServer.read(GetValue, 0, 1535);
				
				if(GetValue[0] == 0)
				{
					System.out.println("Server Disconnected");
					break;
				}
				if(GetValue[1534] == 0)
				{
					System.out.println("Server Send Not Large Enough");
					return;
				}
				
				System.out.println("Sequence_Number: " + Sequence_Number);
				
				if(GetMessage(new String(GetValue)) == 0)
				{
					System.err.println("BAD MAC");
				}
				else
				{
					if(s.length() > 0)
					{
						s += s + " " + Current_Message;
					}
					else
					{
						s = Current_Message;
					}
					Sequence_Number += (Current_Message.length() + 1) / 3;
					System.out.println(s);
				}
				
				System.out.println();
			}
		}
		catch (UnknownHostException ex)
		{
			System.err.println("Client Host Exception: " + ex.getMessage());
		}
		catch (IOException ex)
		{
			System.err.println("Client I/O Exception: " + ex.getMessage());
		}
		catch (Exception ex)
		{
			System.err.println("Client Unknown Exception: " + ex.getMessage());
		}
	}
}

public class Client_SSL
{
	public static void main(String[] args)
	{
		//Need variables
		boolean isTest = false;
		int Port = -1;
		String Server = new String();
		
		//Parse input arguments
		for(int i = 0; i < args.length; i++)
		{
			//Is used for testing, can send commands to socket server
			if(args[i].toLowerCase().compareTo("-test") == 0 || args[i].toLowerCase().compareTo("-debug") == 0)
			{
				//Cannot have two of same command
				if(!isTest)
				{
					isTest = true;
					continue;
				}
				else
				{
					System.err.println("Invalid arguements");
					return;
				}
			}
			//Port Number and server name
			else
			{
				try
				{
					//Cannot have two port numbers
					if(Port == -1)
					{
						Port = Integer.parseInt(args[i].trim());
					}
					else
					{
						System.err.println("Invalid arguements");
						return;
					}
				}
				catch (NumberFormatException ex)
				{
					//Cannot connect to two servers
					if(Server.length() == 0)
					{
						Server = args[i].trim();
					}
					else
					{
						System.err.println("Invalid arguements");
						return;
					}
				}
			}
		}
		
		//missing a server or port number
		if(Server.length() == 0 || Port == -1)
		{
			System.err.println("No Server or Port listed");
			return;
		}
		else
		{
			Client_Protocol P = new Client_Protocol();
			P.ClientSend(Server, Port);
		}
	}
}
