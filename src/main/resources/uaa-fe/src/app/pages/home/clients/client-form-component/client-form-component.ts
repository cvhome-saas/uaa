import {Component, Input, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {ClientsService} from '../services/clients-service';

@Component({
  selector: 'app-client-form-component',
  standalone: false,
  templateUrl: './client-form-component.html',
  styleUrl: './client-form-component.scss',
})
export class ClientFormComponent implements OnInit {
  form!: FormGroup;

  // Options for the select inputs
  clientAuthenticationMethodsOptions = ['client_secret_basic', 'client_secret_post', 'none'];
  authorizationGrantTypesOptions = ['authorization_code', 'refresh_token', 'client_credentials'];
  scopesOptions = ['openid', 'profile', 'api.read'];
  idTokenSignatureAlgorithmOptions = ['RS256', 'ES256'];

  constructor(private fb: FormBuilder,private clientsService:ClientsService) {
  }

  ngOnInit(): void {
    console.log("ashraf")
    this.form = this.fb.group({
      id: [''],
      clientId: [''],
      clientIdIssuedAt: [''],
      clientName: [''],

      clientAuthenticationMethods: [[]],
      authorizationGrantTypes: [[]],

      redirectUris: this.fb.array([]),
      postLogoutRedirectUris: this.fb.array([]),

      scopes: [[]],

      requireProofKey: [false],
      requireAuthorizationConsent: [true],

      accessTokenTimeToLive: [''],
      refreshTokenTimeToLive: [''],
      reuseRefreshTokens: [false],
      idTokenSignatureAlgorithm: ['RS256'],
    });
  }

  @Input() set value(v: any) {
    if (!v) return;

    this.form.patchValue(v);

    this.redirectUris.clear();
    (v.redirectUris || []).forEach((x: string) => this.redirectUris.push(this.createControl(x)));

    this.postLogoutRedirectUris.clear();
    (v.postLogoutRedirectUris || []).forEach((x: string) => this.postLogoutRedirectUris.push(this.createControl(x)));
  }

  createControl(value: string = ''): FormControl {
    return this.fb.control(value);
  }

  get redirectUris() {
    return this.form.get('redirectUris') as FormArray;
  }

  get postLogoutRedirectUris() {
    return this.form.get('postLogoutRedirectUris') as FormArray;
  }

  addRedirectUri() {
    this.redirectUris.push(this.createControl(''));
  }

  removeRedirectUri(i: number) {
    this.redirectUris.removeAt(i);
  }

  addPostLogoutUri() {
    this.postLogoutRedirectUris.push(this.createControl());
  }

  removePostLogoutUri(i: number) {
    this.postLogoutRedirectUris.removeAt(i);
  }

  submit() {
    console.log(this.form.value)
    this.clientsService.save(this.form.value).subscribe();
  }
}
